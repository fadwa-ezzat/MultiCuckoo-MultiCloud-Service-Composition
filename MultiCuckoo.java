
import java.util.LinkedList;

public class MultiCuckoo {
	private LinkedList<Cloud> clouds = new LinkedList<Cloud>();
	private Request request = new Request();
	private int numServicesChecked = 0;

	private ComposedService Lc = new ComposedService();

	public LinkedList<Cloud> getClouds() {
		return this.clouds;
	}

	public void setClouds(LinkedList<Cloud> clouds) {
		this.clouds = clouds;
	}

	public Request getRequest() {
		return this.request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public int getNumServicesChecked() {
		return this.numServicesChecked;
	}

	public void setNumServicesChecked(int numServicesChecked) {
		this.numServicesChecked = numServicesChecked;
	}

	public ComposedService getLc() {
		return this.Lc;
	}

	public void setLc(ComposedService lc) {
		this.Lc = lc;
	}

	public ComposedService composeService() {
		boolean stop = false;
		int cloudsInMCE = getClouds().size() - 1;
		double levy = Levy.sample_positive(2.0D);
		int randomLevy = (int) Math.floor(levy * this.clouds.size()) % this.clouds.size();

		Cloud Cm = getClouds().get(randomLevy); // like birds

		while ((cloudsInMCE >= 0) && (!stop)) {
			LinkedList<Service> cloudServices = Cm.getCloudServices();

			if (cloudServices.size() == 0) {
				cloudServices = MainClass.setServices(Cm.getPort()); // connect to this cloud to access its services
				Cm.setCloudService(cloudServices);
				Cm.setSrvcsSet(true);
			}

			LinkedList<Service> reqCloudIntersect = Helper.cloudRequestIntersect(Cm, getRequest());

			int numOfCloudServices = cloudServices.size();
			int cost = 0;
			for (int i = 0; i < numOfCloudServices; i++) {
				cost += ((Service) cloudServices.get(i)).getCost();
			}
			int num = getNumServicesChecked();
			setNumServicesChecked(num + cost);

			int size = reqCloudIntersect.size();

			if (size > 0) // if the cloud has services meeting the client request
			{
				if (!Helper.intersectWithList(getLc(), reqCloudIntersect)) {
					String savedCloud = "";

					for (int j = 0; j < size; j++) {
						savedCloud = ((Service) reqCloudIntersect.get(j)).getServiceFile();
						if (!getLc().getHashMap().containsKey(savedCloud)) {
							getLc().addToComposedService(savedCloud, Cm);
						}
					}
					if (Helper.foundAllNeededServices(getLc(), getRequest())) {
						stop = true;
					}
				}
			}

			if ((cloudsInMCE >= 0) && (!stop)) {
				if (randomLevy < getClouds().size()) { // fly again and get a new Cloud
					levy = Levy.sample_positive(2.0D);
					randomLevy = (int) Math.floor(levy * this.clouds.size()) % this.clouds.size();

					Cm = getClouds().get(randomLevy);
				}
				cloudsInMCE--;
			}
		}

		return getLc();
	}
}
