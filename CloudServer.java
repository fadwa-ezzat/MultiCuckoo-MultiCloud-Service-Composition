
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * 
 * @author Fadwa Ezzat This class creates a Thread for each cloud
 * Instantiated by GenerateServicesConfig.java
 */
public class CloudServer extends Thread {
	private String name;
	private Integer port;
	private String[] services;
	private boolean running;
	private ServerSocket listenSocket = null;

	public CloudServer(String name, Integer port, String[] services) {
		this.name = name;
		this.port = port;
		this.services = services;
		this.running = true;
	}

	public synchronized void terminate() {
		this.running = false;
		if (this.listenSocket != null)
			try {
				this.listenSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void run() {
		try {
			this.listenSocket = new ServerSocket(this.port.intValue());
			System.out.println(
					this.name + " started on port: " + this.port + ", for services: " + Arrays.toString(this.services));

			while (this.running) {
				Socket clientSocket = this.listenSocket.accept();
				ServerConnection c = new ServerConnection(clientSocket, this.name, this.services);
				c.start();
			}
		} catch (IOException localIOException) {
			if (this.listenSocket != null)
				try {
					this.listenSocket.close();
				} catch (IOException localIOException1) {
				}
		} finally {
			if (this.listenSocket != null)
				try {
					this.listenSocket.close();
				} catch (IOException localIOException2) {
				}
		}
	}
}