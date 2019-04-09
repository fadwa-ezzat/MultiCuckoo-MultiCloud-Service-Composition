
import java.util.LinkedList;

public class Request {
	private LinkedList<Service> requestServices = new LinkedList();

	public Request(LinkedList<Service> requestServices) {
		this.requestServices = requestServices;
	}

	public Request() {
	}

	public LinkedList<Service> getRequestServices() {
		return this.requestServices;
	}

	public void setRequestService(LinkedList<Service> request) {
		this.requestServices = request;
	}
}