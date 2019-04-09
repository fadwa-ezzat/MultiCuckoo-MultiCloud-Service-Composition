public class Service {
	private String serviceFile = "";
	private int cost = 0;

	public Service(String serviceFile, int cost) {
		this.serviceFile = serviceFile;
		this.cost = cost;
	}

	public Service(String serviceFile) {
		this.serviceFile = serviceFile;
	}

	public Service() {
	}

	public String getServiceFile() {
		return this.serviceFile;
	}

	public void setServiceFile(String fileName) {
		this.serviceFile = fileName;
	}

	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}
