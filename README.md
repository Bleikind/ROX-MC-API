# ROX-MC-API
A api to send informations from a minecraft server to my rox server

<h1>Create Client</h1><br>

<blockquote><pre>
        ROXNetwork roxNetwork = new ROXNetwork("localhost", 8982, UUID.randomUUID(), "mySecretPassword");
        if(roxNetwork.connect()){
            getLogger().info("Connected to ROX Server!");
        }else{
            getLogger().warning("Error to connect to server :(");
        }
        roxNetwork.close();
</pre></blockquote>

<h1>Set Information</h1><br>

How to send informations to the rox server:
<blockquote><pre>
        ROXNetwork roxNetwork = new ROXNetwork("localhost", 8982, UUID.randomUUID(), "mySecretPassword");
        if(roxNetwork.connect()){
            getLogger().info("Connected to ROX Server!");
        }else{
            getLogger().warning("Error to connect to server :(");
        }
        roxNetwork.send("playerSize", Bukkit.getOnlinePlayers().size());
        roxNetwork.close();
</pre></blockquote>

<br>
<h1>Get Information</h1>
<br>How you can get the last information send to you:
(The information will directly removed from index)

<blockquote><pre>
        ROXNetwork roxNetwork = new ROXNetwork("localhost", 8982, UUID.randomUUID(), "mySecretPassword");
        if(roxNetwork.connect()){
            getLogger().info("Connected to ROX Server!");
        }else{
            getLogger().warning("Error to connect to server :(");
        }
        String lastInfo = roxNetwork.getLastElement();
        if(lastInfo == null) return;
        switch (lastInfo.toLowerCase()){
            case "playersize":
                roxNetwork.send(lastInfo, Bukkit.getOnlinePlayers().size());
                break;
            default:
                getLogger().warning("Could not recognise unknown command.");
                break;
        }
        roxNetwork.close();
</pre></blockquote>

If you want to get the first element from the list:

<blockquote><pre>
        ROXNetwork roxNetwork = new ROXNetwork("localhost", 8982, UUID.randomUUID(), "mySecretPassword");
        if(roxNetwork.connect()){
            getLogger().info("Connected to ROX Server!");
        }else{
            getLogger().warning("Error to connect to server :(");
        }
        String lastInfo = roxNetwork.getFirstElement();
        if(lastInfo == null) return;
        switch (lastInfo.toLowerCase()){
            case "playersize":
                roxNetwork.send(lastInfo, Bukkit.getOnlinePlayers().size());
                break;
            default:
                getLogger().warning("Could not recognise unknown command.");
                break;
        }
        roxNetwork.close();
</pre></blockquote>


