import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Lab3Server {

    public static void main(String[] args) throws IOException{
        int portNumber = 5555;
        if (args.length > 0){
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else{
                System.err.println("Usage: java EchoUcaseServerTCP [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am EchoUCase TCP server");
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket connectSocket = serverSocket.accept();
                PrintWriter out =new PrintWriter(connectSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));)
        {
            InetAddress clientAddr = connectSocket.getInetAddress();

            int clientPort = connectSocket.getPort();
            String receivedText;

            while ((receivedText = in.readLine())!= null) {
                System.out.println("Client [" + clientAddr.getHostAddress() +  ":" + clientPort +"] > " + receivedText);
                String outText = receivedText.toUpperCase();
                out.println(outText);System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + portNumber + "] > " + outText);
            }
            System.out.println("I am done, Bye!");
        }
        catch (IOException e){
            System.out.println("Exception caught when trying to listen on port "+ portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}