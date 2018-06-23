package bleikind;

import java.io.*;
import java.net.Socket;
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

    public ROXNetwork(String hostname, int port, UUID minecraftServerUUID, String password){
        this.hostname = hostname;
        this.password = password;
        this.port = port;
        this.minecraftServerUUID = minecraftServerUUID;
    }

    public boolean connect(){
        try {
            socket = new Socket(hostname, port);

            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println(minecraftServerUUID + "§" + password);

            String answer = reader.readLine();
            switch(answer){
                case "§UUID_NOT_FOUND":
                        ROXApi.getInstance().getLogger().warning("Could not connect to ROX Server. UUID is not registered.");
                    break;

                case "§ACCOUNT_NOT_FOUND":
                        ROXApi.getInstance().getLogger().warning("Could not connect to ROX Server. UUID or password is wrong.");
                    break;

                case "§MC_SERVER_CONNECTED":
                    (inputThread = new Thread(ROXNetwork::inputThread)).start();
                    return true;

                default:
                    ROXApi.getInstance().getLogger().warning("Is the api on the newest version? Because i can not handle this information: " + answer);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Socket getSocket(){
        return socket;
    }

    public Thread getInputThread(){
        return inputThread;
    }

    private static void inputThread() {
        try{
            String input;
            while((input = reader.readLine()) != null){
                if(input.startsWith("§")){
                    ROXApi.getInstance().getLogger().warning("Error doing reading input: " + input.substring(1));
                    return;
                }
                index.add(input);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(String key, Object value){
        writer.println("§" + key + "§" + value.toString());
    }

    public String getFirstElement(){
        String last = index.get(0);
        index.remove(0);
        return last;
    }

    public String getLastElement(){
        String last = index.get(index.size());
        index.remove(index.size());
        return last;
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
