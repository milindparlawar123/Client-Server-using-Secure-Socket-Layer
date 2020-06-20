import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
	public static void main(String args[]) {
		System.setProperty("javax.net.ssl.keyStore", "parlawarmilind.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		try {
			SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(50001);
			System.out.println("Server Started & listening");
			SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
			BufferedReader input = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
			PrintWriter writeToClient = new PrintWriter(sslSocket.getOutputStream(), true);
	
				String recivedMessage = input.readLine();
				String readLine = null;
				FileProcessor fileProcessor = new FileProcessor("password.txt");
				
				boolean isIdPassMat=false;
				while ((readLine = fileProcessor.poll()) != null) {
					if (readLine.split(" ")[0].equals(recivedMessage.split(" ")[0])
							&& readLine.split(" ")[1].equals(recivedMessage.split(" ")[1])) {
						isIdPassMat=true;	
					}					
				}
				if(isIdPassMat) {
					writeToClient.println("correct ID and password");
				}else {
					writeToClient.println("incorrect ID and password");
				}
				writeToClient.close();
				input.close();
				sslSocket.close();
				sslServerSocket.close();
				fileProcessor.close();
		} catch (Exception ex) {
			System.err.println("Error Happened : " + ex.toString());
		}
	}
}

class FileProcessor {
	private BufferedReader reader;
	private String line;

	public FileProcessor(String inputFilePath)
			throws InvalidPathException, SecurityException, FileNotFoundException, IOException {

		if (!Files.exists(Paths.get(inputFilePath))) {
			throw new FileNotFoundException("fild not found");
		}

		reader = new BufferedReader(new FileReader(new File(inputFilePath)));
		line = reader.readLine();
		if (line == null) {
			System.out.println(inputFilePath + "empty file");
			System.exit(0);
		}
	}

	public String poll() throws IOException {
		if (null == line)
			return null;

		String newValue = line.trim();
		line = reader.readLine();
		return newValue;
	}

	public void close() throws IOException {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOException("error while closing file", e);
		} finally {

		}
	}
}
