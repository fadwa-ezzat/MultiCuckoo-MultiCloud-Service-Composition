import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Fadwa Ezzat This class is used to simulate the connection request to
 *         a cloud on its port
 */
public class TCPClient {
	public static String connectToCloud(int port, String service) {
		Socket socket = null;
		try {
			int serverPort = port;
			String ip = "localhost";
			String data = service;

			socket = new Socket(ip, serverPort);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			output.writeInt(data.length());
			output.writeBytes(data);
			output.flush();

			int len = input.readInt();
			byte[] inputBytes = new byte[len];
			for (int i = 0; i < len; i++) {
				inputBytes[i] = input.readByte();
			}
			String response = new String(inputBytes);
			System.out.println("Received a request to cloud on port " + serverPort + " Its response is: " + response);
			return response;
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException localIOException5) {
				}
		}
		return null;
	}
}