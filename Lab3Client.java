import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Lab3Client {
    public static void main(String[] args) throws IOException{
        String hostName = "127.0.0.1";
        int portNumber = 5555;
        if (args.length > 0){
            hostName = args[0];
            if (args.length > 1){
                portNumber = Integer.parseInt(args[1]);
                if (args.length > 2){
                    System.err.println("Usage: java EchoClientTCP [<host name>] [<port number>]");
                    System.exit(1);
                }
            }
        }
        System.out.println("Hi, I am EchoUCase TCP client!");
        try(
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter out =new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in)))
        {
            String userInput;
            System.out.print("I (Client) [" + InetAddress.getLocalHost()  + ":" + clientSocket.getLocalPort() + "] > ");
            while ((userInput = stdIn.readLine()) != null && !userInput.isEmpty()){
                out.println(userInput);
                String receivedText = in.readLine();
                System.out.println("Server [" + hostName +  ":" + portNumber + "] > " + receivedText);
                System.out.print("I (Client) [" + clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort() + "] > ");
            }
        } catch (UnknownHostException e){System.err.println("Unknown host " + hostName);
        System.exit(1);
        } catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}

