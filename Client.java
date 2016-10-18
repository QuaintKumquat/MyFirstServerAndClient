import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Socket sock = null;
		String host = "172.17.22.50";
		int port = 14641;
		
		boolean success = false;
		int attempts = 0;//keeps track of attempts to establish connection
		
		while(!success && attempts < 10){//tries ten times to create the server
			attempts++;
			try{
				System.out.println("Creating Socket for "+host+":"+port+" (Attempt "+attempts+")");
				sock = new Socket(host,port);
				success = true;
				System.out.println("Socket Created and connected to Server!");
				
				DataInputStream in = new DataInputStream(sock.getInputStream());
				DataOutputStream out = new DataOutputStream(sock.getOutputStream());
				Scanner scan = new Scanner(System.in);
				String output = scan.nextLine();
				out.writeUTF(output);
				while(!output.equalsIgnoreCase("close") && !output.equalsIgnoreCase("exit")){
					System.out.println("Server: "+in.readUTF());
					output = scan.nextLine();
					out.writeUTF(output);
				}
			}
			catch(UnknownHostException e){
				System.out.println("Could not find the host.");
			}
			catch(IOException e){
				System.out.println("Could not create Socket for "+host+":"+port);
			}
		}
		if(success){
			try {
				sock.close();
				System.out.println("Socket Closed");
			}catch(IOException e){
				System.out.println("Uh oh, could not close Socket");
			}
		}
		else{
			System.out.println("Giving up...");
		}
		
		
		
	}

}
