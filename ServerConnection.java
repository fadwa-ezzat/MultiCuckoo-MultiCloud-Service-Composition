import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * @author Fadwa Ezzat
 * This class is instantiated by CloudServer class in the run to run clouds on separate ports
 * 
 */
class ServerConnection extends Thread {
	private static final long RESPONSE_WAIT_TIME = 1000L;
	private DataInputStream input;
	private DataOutputStream output;
	private Socket clientSocket;
	private String[] availableServices;

	public ServerConnection(Socket aClientSocket, String name, String[] services) {
		try {
			this.clientSocket = aClientSocket;
			this.input = new DataInputStream(this.clientSocket.getInputStream());
			this.output = new DataOutputStream(this.clientSocket.getOutputStream());
			this.availableServices = services;
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			int len = this.input.readInt();
			byte[] inputBytes = new byte[len];
			for (int i = 0; i < len; i++) {
				inputBytes[i] = this.input.readByte();
			}
			String requestedService = new String(inputBytes);
			byte[] data;
			if (requestedService.equals("list")) {
				StringBuilder builder = new StringBuilder();
				for (String srv : this.availableServices) {
					builder.append(srv + ",");
				}
				data = builder.toString().getBytes();
			} else {
				boolean found = false;
				for (String srv : this.availableServices)
					if (srv.contains(requestedService)) {
						found = true;
						break;
					}
				if (found) {
					String filePath = "C://" + requestedService + ".txt";
					Path path = Paths.get(filePath, new String[0]);
					data = Files.readAllBytes(path);
				} else {
					data = new String(requestedService + " Not Available").getBytes();
				}

			}

			Thread.sleep(RESPONSE_WAIT_TIME);

			this.output.writeInt(data.length);
			this.output.write(data);

			this.output.flush();
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
			try {
				/* 103 */ this.clientSocket.close();
			} catch (IOException localIOException1) {
			}
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
			try {
				/* 103 */ this.clientSocket.close();
			} catch (IOException localIOException2) {
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			try {
				/* 103 */ this.clientSocket.close();
			} catch (IOException localIOException3) {
			}
		} finally {
			try {
				this.clientSocket.close();
			} catch (IOException localIOException4) {
			}
		}
	}
}