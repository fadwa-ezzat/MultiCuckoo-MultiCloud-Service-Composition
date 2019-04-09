
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) { // the cloud threads should be running so as to connect to
												// them on their port and request their service files
		LinkedList<DynamicList> dynamicList = new LinkedList<DynamicList>();
		LinkedList<Cloud> totalClouds = new LinkedList<Cloud>();
		LinkedList<Cloud> cuckooClouds = new LinkedList<Cloud>();
		try {
			// reading config.txt
			BufferedReader br = new BufferedReader(
					new FileReader(System.getProperty("user.dir") + "//..\\CloudSim\\config.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] lineSplits = line.split(":");
				int port = Integer.parseInt(lineSplits[1]);
				if (lineSplits.length == 3) {
					Cloud cloud = new Cloud("localhost", Integer.valueOf(port));
					totalClouds.add(cloud);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> toFile = new LinkedList<String>();// a variable to store the attributes and results to be written
														// to the excel file
		toFile.add("request");
		toFile.add("MC Clds combined");
		toFile.add("MC Srv Cost");
		toFile.add("MC time");

		toFile.add("\n");
		while (true) {
			System.out.println("\n Enter the user request separated by spaces \n or press 'q' to exit");
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			if (input.equalsIgnoreCase("q"))
				return;
			String request = "";
			String requests = "";
			LinkedList<Service> requestServices = new LinkedList<Service>();
			for (int j = 0; j < input.split(" ").length; j++) {
				request = input.split(" ")[j];
				requests = requests + request + " ";
				requestServices.add(new Service(request));
			}
			Request userRequest = new Request(requestServices);
			toFile.add(requests);

			// Here starts the code for MultiCuckoo

			long startTime = System.nanoTime();
			MultiCuckoo multiCuckoo = new MultiCuckoo();
			multiCuckoo.setRequest(userRequest);

			if (dynamicList.size() == 0)
				DynamicList.time = new Date().getTime();

			long difference = new Date().getTime() - DynamicList.time;
			long diffMinutes = difference / 60000L % 60L;

			if (diffMinutes > 50L) // 50 seconds have passed
				dynamicList = new LinkedList<DynamicList>();

			DynamicList composedList = Helper.similarRequest(dynamicList, userRequest.getRequestServices());
			int size = composedList.getLSRequest().getRequestServices().size();

			if (size > 0) { // was a composed list of services for the new request created from before for
							// past requests?
				System.out.println(
						"For MultiCuckoo, request similar to one saved from before,so examined services and combined clouds = 0 ");
				toFile.add("0"); // zero combined clouds
				toFile.add("0"); // zero examined services
				toFile.add("0"); // zero time taken
			} else { // previous composed services can not fulfill new request? create a new composed
						// service
				int port = 0;
				// start service composition
				for (Object cloud : totalClouds)
					cuckooClouds.add(new Cloud((Cloud) cloud));

				multiCuckoo.setClouds(cuckooClouds);
				ComposedService composed = multiCuckoo.composeService();
				if (composed.composedSize() > 0) { // found a service composition sequence to fulfill user request
					dynamicList.add(new DynamicList(userRequest, composed)); // save the request and its composed
																				// service for any future requests

					HashMap<String, Cloud> servicesMap = composed.getHashMap();
					int numClouds = 0;
					LinkedList<Integer> cloudsCombined = new LinkedList<Integer>();

					for (Object cloud : servicesMap.values()) {
						port = ((Cloud) cloud).getPort();
						if (!cloudsCombined.contains(Integer.valueOf(port))) {
							numClouds++;
							cloudsCombined.add(Integer.valueOf(port)); // the value unique to each cloud thread is its
																		// port number
						}
					}

					int services = multiCuckoo.getNumServicesChecked();
					toFile.add(numClouds + "");
					toFile.add(services + "");
					toFile.add(System.nanoTime() - startTime + "");
				} else {
					// did not find a sequence
					toFile.add("---");
					toFile.add("---");
					toFile.add("---");
					System.out.println("services not found in clouds");
				}
			}

			// Here ends the code for MultiCuckoo

			Helper.writeToCSV(toFile);// write the statistics to file so as to draw charts and compare
			toFile = new LinkedList<String>();
		}
	}

	public static LinkedList<Service> setServices(int port) {
		LinkedList<Service> services = new LinkedList<Service>();
		String cloudServices = TCPClient.connectToCloud(port, "list");

		for (String service : cloudServices.split(",")) {
			String[] split = service.split("-");// service name and cost
			Service srv = new Service(split[1], Integer.parseInt(split[0]));
			services.add(srv);
		}
		return services;
	}
}
