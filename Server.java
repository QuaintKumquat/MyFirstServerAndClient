import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server {

	public static void main(String[] args) {
		ServerSocket serverSock = null;//the 'server' that will wait for a client socket to connect
		int port = 14641;
		boolean success = false;
		int attempts = 0;//keeps track of attempts to establish connection
		
		while(!success && attempts < 10){//tries ten times to create the server
			attempts++;
			try{
				System.out.println("Creating ServerSocket at port "+port+" (Attempt "+attempts+")");
				serverSock = new ServerSocket(port);//throws IOException if port is taken
				success = true;
				System.out.println("ServerSocket Created!");
			}
			catch(IOException e){
				System.out.println("Could not create ServerSocket at port "+port);
			}
		}
		if(success){//successfully create the ServerSocket object
			
			//set timeout delay
			try {
				serverSock.setSoTimeout(60000);//sets time to wait for client to 60 seconds
			}
			catch(SocketException e){//thrown if the socket is bad
				System.out.println("Uh oh, all I did was try to set the timeout delay.");
			}
			
			//wait for client to connect
			try {
				System.out.println("Waiting for client to connect...");
				Socket sock = serverSock.accept();
				System.out.println("Socket accepted.");
				System.out.println(sock.isConnected()?"Socket connected.":"Socket not connected.");
				
				DataInputStream in = new DataInputStream(sock.getInputStream());
				DataOutputStream out = new DataOutputStream(sock.getOutputStream());
				String input = in.readUTF();
				System.out.println("Client: "+input);
				while(!input.equalsIgnoreCase("close") && !input.equalsIgnoreCase("exit")){
					out.writeUTF("Pong!");
					System.out.println("Server: Pong!");
					input = in.readUTF();
					System.out.println("Client: "+input);
				}
				
				
				//input = in.readUTF();
			}
			catch(SocketTimeoutException e){//no clients connects within the timeout delay
				System.out.println("Nobody wanted to connect.");
			}
			catch(IOException e){
				System.out.println("IOException during accept()");
			}
			
			try {
				serverSock.close();
				System.out.println("Server Closed");
			}catch(IOException e){
				System.out.println("Tried to close the server, we've got problems.");
			}
		}
		else{
			System.out.println("Giving up...");
		}
		

	}

}
