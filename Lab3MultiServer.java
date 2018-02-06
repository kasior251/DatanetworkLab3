import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Lab3MultiServer {

    public static void main(String[] args) throws IOException {

        int portNumber = 5555;

        if (args.length > 0) {
            if (args.length == 1) {
                portNumber = Integer.parseInt(args[0]);
            }
            else {
                System.err.println("Usage: java MultiServerConverter [<port number>]");
                System.exit(1);
            }
        }


        System.out.println("Hi I am the TCP MultiServerConverter");
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);

                )
        {
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

        //csv file location with currency rates
        String file = "C:\\Users\\Kasia\\IdeaProjects\\DatanetworkLab3Part1\\src\\currencies.csv";
        BufferedReader br = null;
        String line = "";
        String splitBy = ",";
        String[] currency = new String[10];

        public ClientServer(Socket connectSocket) {

            this.connectSocket = connectSocket;
            clientAddres = connectSocket.getInetAddress();
            clientPort = connectSocket.getPort();
            serverPort = connectSocket.getLocalPort();

            try {
                br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    currency = line.split(splitBy);
                }
            }
            catch (FileNotFoundException e) {
                System.err.println("File with currency rates not found");
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
                    String outText = Converter.convert(receivedText.toUpperCase(), currency);
                    out.println(outText);
                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort +"]> " + outText);
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
