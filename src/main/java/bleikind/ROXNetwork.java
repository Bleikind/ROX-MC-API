package bleikind;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;

public class ROXNetwork {

    private Socket socket;

    private String hostname, password;

    private UUID minecraftServerUUID;

    private static PrintWriter writer;

    private static BufferedReader reader;

    private int port;

    private Thread inputThread;

    private static ArrayList<String> index = new ArrayList<>();

    public ROXNetwork(String hostname, int port, UUID minecraftServerUUID, String password) {
        this.hostname = hostname;
        this.password = password;
        this.port = port;
        this.minecraftServerUUID = minecraftServerUUID;
    }

    public boolean connect() {
        try {
            socket = new Socket(hostname, port);

            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            writer.println("MINECRAFT" + "§" + ROXApi.getInstance().getVersion() + "§" + minecraftServerUUID + "§" + computeSHA256(password));

            String answer = reader.readLine();
            switch (answer) {
                case "CONNECTION_WRONG_VERSION":
                    ROXApi.getInstance().getLogger().warning("Could not connect to ROX Server. Wrong version. Please update your plugin.");
                    break;

                case "CONNECTION_REFUSED":
                    ROXApi.getInstance().getLogger().warning("Could not connect to ROX Server. UUID or password is wrong.");
                    break;

                case "CONNECTION_ACCEPTED":
                    (inputThread = new Thread(ROXNetwork::inputThread)).start();
                    ROXApi.getInstance().getLogger().info("Connected to ROX Server.");
                    return true;

                default:
                    ROXApi.getInstance().getLogger().warning("Is the api on the newest version? Because i can not handle this information: " + answer);
                    break;
            }

        } catch (Exception e) {
            if (e instanceof SocketException) {
                ROXApi.getInstance().getLogger().warning("Could not connect to ROX Server.");
            }
            e.printStackTrace();
        }

        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    public Thread getInputThread() {
        return inputThread;
    }

    private static void inputThread() {
        try {
            String input;
            while ((input = reader.readLine()) != null) {
                if (input.startsWith("§")) {
                    ROXApi.getInstance().getLogger().warning("Error doing reading input: " + input.substring(1));
                    return;
                }
                index.add(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ask(String ask) {
        writer.println("?" + ask);
    }

    public void send(String key, Object value) {
        writer.println("§" + key + "§" + value.toString());
    }

    public String getFirstElement() {
        String last = index.get(0);
        index.remove(0);
        return last;
    }

    public String getLastElement() {
        String last = index.get(index.size());
        index.remove(index.size());
        return last;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String computeSHA256(String string) {
        try {
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : MessageDigest.getInstance("SHA-256").digest(string.getBytes("UTF-8"))) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return port;
    }

    public static BufferedReader getReader() {
        return reader;
    }

    public static PrintWriter getWriter() {
        return writer;
    }

    public String getHostname() {
        return hostname;
    }

    public static ArrayList<String> getIndex() {
        return index;
    }

    public UUID getMinecraftServerUUID() {
        return minecraftServerUUID;
    }

    public static void setIndex(ArrayList<String> index) {
        ROXNetwork.index = index;
    }

    public static void setWriter(PrintWriter writer) {
        ROXNetwork.writer = writer;
    }

    public static void setReader(BufferedReader reader) {
        ROXNetwork.reader = reader;
    }

    public void setInputThread(Thread inputThread) {
        this.inputThread = inputThread;
        if(!inputThread.isAlive())inputThread.start();
    }
}
