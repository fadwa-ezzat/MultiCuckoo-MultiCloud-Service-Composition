
import java.util.LinkedList;

public class Cloud {
	private LinkedList<Service> cloudServices = new LinkedList<Service>();
	private int port = 0;
	private String host = "";
	private boolean srvcsSet = false;

	public Cloud(Cloud cloud) {
		this.port = cloud.getPort();
		this.host = cloud.getHost();
	}

	public Cloud() {
		this.cloudServices = new LinkedList<Service>();
		this.srvcsSet = false;
	}

	public Cloud(String host, Integer port) {
		this.cloudServices = new LinkedList<Service>();
		this.host = host;
		this.port = port.intValue();
		this.srvcsSet = false;
	}

	public Cloud(String host, Integer port, LinkedList<Service> list) {
		this.cloudServices = list;
		this.host = host;
		this.port = port.intValue();
		this.srvcsSet = false;
	}

	public int numOfActualServices() {
		LinkedList<?> itsServices = getCloudServices();
		int size = 0;
		for (int i = 0; i < itsServices.size(); i++) {
			size += ((Service) itsServices.get(i)).getCost();
		}
		return size;
	}

	public LinkedList<Service> getCloudServices() {
		return this.cloudServices;
	}

	public void addCloudService(Service service) {
		this.cloudServices.add(service);
	}

	public void setCloudService(LinkedList<Service> service) {
		this.cloudServices = service;
		this.srvcsSet = true;
	}

	public boolean getSrvcsSet() {
		return this.srvcsSet;
	}

	public void setSrvcsSet(boolean flag) {
		this.srvcsSet = flag;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}