import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class Lab3MultiServer {

    public static void main(String[] args) throws IOException {

        int portNumber = 5555;

        if (args.length > 0) {
            if (args.length == 1) {
                portNumber = Integer.parseInt(args[0]);
            }
            else {
                System.err.println("Usage: java EchoUcaseServerMutiClients [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi I am the Multi Client TCP Server");
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                )
        {
            String receivedText;

            //continuosly listening for clients
            while (true) {
                //start a new ClientServer thread for each client
                ClientServer clientServer = new Lab3MultiServer.ClientServer(serverSocket.accept());
                clientServer.start();
            }

        }
        catch (IOException e) {
            System.out.println("Exception occurred when trying to listen on port "+ portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    //this class serves clients in separate threads
    static class ClientServer extends Thread {
        Socket connectSocket;
        InetAddress clientAddres;
        int serverPort, clientPort;

        public ClientServer(Socket connectSocket) {
            this.connectSocket = connectSocket;
            clientAddres = connectSocket.getInetAddress();
            clientPort = connectSocket.getPort();
            serverPort = connectSocket.getLocalPort();
        }

        public void run() {
            try (
                    PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
                    )
            {
                String receivedText;
                while (((receivedText = in.readLine()) != null)) {
                    System.out.println("Client [" + clientAddres.getHostAddress() +  ":" + clientPort +"] > " + receivedText);
                    String outText = receivedText.toUpperCase();
                    out.println(outText);
                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort +"] > " + outText);
                }
                connectSocket.close();
            }
            catch (IOException e) {
                System.out.println("Exception occurred when trying to communicate with the client " + clientAddres.getHostAddress());
                System.out.println(e.getMessage());
            }
        }
    }
}
