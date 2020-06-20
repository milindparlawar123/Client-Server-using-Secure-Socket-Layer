import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {
	public static void main(String args[]) {
		System.setProperty("javax.net.ssl.trustStore", "milindclienttruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		try {
			if(args.length != 1) {
				System.out.println("please provide argument");
				System.exit(0);
			}
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(args[0], 50001);
			System.out.println("Enter ID and password");
			PrintWriter writeToServer = new PrintWriter(sslSocket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
			//System.out.println(input.readLine());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				//System.out.println("Write a Message : ");
				String messageToSend = bufferedReader.readLine();
				writeToServer.println(messageToSend);

				System.out.println(input.readLine());
				
		} catch (Exception ex) {
			System.err.println("Error Happened : " + ex.toString());
		}
	}
}