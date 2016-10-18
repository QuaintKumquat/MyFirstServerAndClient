import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class Server3 {

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
				
				InputStream in = sock.getInputStream();
				OutputStream out = sock.getOutputStream();
				Scanner scan = new Scanner(in,"UTF-8");
				String key = "";
				String input = "";
				//handshake
				boolean cont = true;
				
				while(cont){//key.equals("")){
					//System.out.println("Next line...");
					input = scan.nextLine();
					if(input.matches("Sec-WebSocket-Key: .+")) key = input.substring(19);
					System.out.println("Client: "+input);
					cont = !input.equals("") && !input.equals(" ") && scan.hasNextLine();
				}
				//System.out.println("Closing scan");
				//scan.close();
				System.out.println("Recieved handshake");
				String response = ""
						+ "HTTP/1.1 101 Switching Protocols"+"\r\n"
						+ "Upgrade: websocket"+"\r\n"
						+ "Connection: Upgrade"+"\r\n"
						+ "Sec-WebSocket-Accept: ";
				
				key+="258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
				System.out.println("key: "+key);
				try {
					response += DatatypeConverter.printBase64Binary(MessageDigest.getInstance("SHA1").digest(key.getBytes("UTF-8")))+"\r\n\r\n";
				}catch (NoSuchAlgorithmException e){System.out.println("SHA1 no longer exists...");}
				byte b[] = response.getBytes("UTF-8");
				System.out.println("Response ready, "+b.length);
				
				out.write(b,0,b.length);
				System.out.println("Shook hands");
				//out.write((byte)(1));
				//out.write((byte)(5 + 128));
				//out.write("Hello".getBytes("UTF-8"));
				//System.out.println("Server: Hello");
				String data = "";
				int dat = in.read();
				while(dat != -1){
					data+=dat+" ";
					dat = in.read();
				}
				System.out.println("Client: "+ data);
				//out.writeUTF("Hello!");
				//System.out.println("Server: Hello!");
					
				
				//input = in.readUTF();
			}
			catch(SocketTimeoutException e){//no clients connects within the timeout delay
				System.out.println("Nobody wanted to connect.");
			}
			catch(IOException e){
				System.out.println("IOException during accept()");
				e.printStackTrace();
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