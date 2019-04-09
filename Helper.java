
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Helper {
	// the similarity between a new request and a previously received request
	public static double similarityRatio = 0.8D;

	public static DynamicList similarRequest(LinkedList<DynamicList> dynamicList, LinkedList<Service> newRequest) {
		double currentMaxRatio = 0.0D;
		DynamicList currentRequest = new DynamicList();
		for (int i = 0; i < dynamicList.size(); i++) {
			DynamicList list = dynamicList.get(i);
			LinkedList<Service> listRequest = list.getLSRequest().getRequestServices();

			double ratio = compareRequests(listRequest, newRequest);
			if ((ratio >= similarityRatio) && (ratio >= currentMaxRatio)) {
				currentMaxRatio = ratio;
				currentRequest = list;
			}
		}
		return currentRequest;
	}

	public static double compareRequests(LinkedList<Service> listRequest, LinkedList<Service> newRequest) {
		double ratio = 0.0D;
		for (Service ser : newRequest) {
			boolean found = false;
			for (Service cache : listRequest) {
				if (ser.getServiceFile().equalsIgnoreCase(cache.getServiceFile())) {
					found = true;
					break;
				}
			}

			if (!found) {
				ratio = 0.0D;
				break;
			}
			ratio += 1.0D / listRequest.size();
		}

		return ratio;
	}

	// create a file in the working directory to save statistics to
	public static void writeToCSV(List<String> toFile) {
		try {
			FileWriter pw = new FileWriter(new File("analytics.csv"), true);
			StringBuilder sb = new StringBuilder();

			for (String string : toFile) {
				sb.append(string);
				if (!string.equals("\n"))
					sb.append(",");
			}
			sb.append('\n');
			pw.write(sb.toString());
			pw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static LinkedList<Service> cloudRequestIntersect(Cloud Cm, Request request) {
		LinkedList<Service> requestServices = request.getRequestServices();
		LinkedList<Service> cloudServices = Cm.getCloudServices();
		LinkedList<Service> intersectingServices = new LinkedList<Service>();

		Service cloudService = new Service();
		Service requestService = new Service();
		String cloudFile = "";
		String requestFile = "";

		int requestLength = requestServices.size();
		for (int i = 0; i < requestLength; i++) {
			requestService = (Service) requestServices.get(i);
			requestFile = requestService.getServiceFile();
			for (int j = 0; j < cloudServices.size(); j++) {
				cloudService = (Service) cloudServices.get(j);
				cloudFile = cloudService.getServiceFile();

				if (requestFile.equalsIgnoreCase(cloudFile)) {
					intersectingServices.add(cloudService);
				}
			}
		}
		return intersectingServices;
	}

	public static boolean foundAllNeededServices(ComposedService lc2, Request request2) {
		Service inRequest = new Service();
		LinkedList<Service> requestService = request2.getRequestServices();
		HashMap<String, Cloud> composed = lc2.getHashMap();
		boolean found = true;
		int size = requestService.size();
		for (int i = 0; i < size; i++) {
			inRequest = (Service) requestService.get(i);

			if (!composed.containsKey(inRequest.getServiceFile())) {
				return !found;
			}
		}

		return found;
	}

	public static boolean intersectWithList(ComposedService Lc, LinkedList<Service> cloudServices) {
		Service service = new Service();
		int length = cloudServices.size();

		for (int i = 0; i < length; i++) {
			service = cloudServices.get(i);
			String file = service.getServiceFile();
			Cloud cloud = Lc.getComposed(file);
			LinkedList<Service> cloudServ = cloud.getCloudServices();
			if (cloudServ.size() == 0) {
				return false;
			}
		}

		return true;
	}
}