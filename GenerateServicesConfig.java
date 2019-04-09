import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Fadwa Ezzat This is the server side main class; generates the clouds,
 *         their service files and services and stores it to
 *         "config.txt"
 */

public class GenerateServicesConfig {
	// the maximum number of clouds in the multicloud environment
	public static int CLOUD_NUM = 100;
	// the maximum number of services found in the multicloud environment
	public static int SERVICE_FILE_MAX = 50;
	public static int SERVICE_MAX = 50; // the maximum number of services a cloud can have

	// this file stores the clouds and their services
	public static final String FILENAME = System.getProperty("user.dir") + "\\config.txt";
	public static final int INTERVAL = 900000000; // tested many numbers for thread sleep and chose this :-D
	public static HashMap<String, CloudServer> clouds;

	/**
	 * This function generates a set of cloud, each having a set of service files
	 * containing a number of services It ensures that if a service file "f01" in a
	 * cloud has 25 services, it will have the same number of services if it was to
	 * be found in another cloud so, if cloud01 has f01 which has 25 services and
	 * cloud03 also has f01, it will have the same 25 services
	 * 
	 * @param cloudNum
	 * @param serviceFileMax
	 * @param serviceMax
	 * @param fileName
	 * @throws IOException
	 */
	public static void Generate(int cloudNum, int serviceFileMax, int serviceMax) {
		CLOUD_NUM = cloudNum;
		SERVICE_FILE_MAX = serviceFileMax;
		SERVICE_MAX = serviceMax;
		FileWriter fw = null;
		BufferedWriter bw = null;

		// stores the service file names
		HashMap sfMap = new HashMap();
		try {
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			StringBuilder builder = new StringBuilder();

			for (int c = 0; c < CLOUD_NUM; c++) {
				// writing a line in the file
				String cloudName = String.format("%02d", new Object[] { Integer.valueOf(c + 1) });
				// the cloud names and ports are written in order
				builder.append("CloudServer" + cloudName + ":" + (7800 + c + 1) + ":");
				// creating clouds and writing their names and ports to file
				Random rand = new Random();
				int sf_num = rand.nextInt(SERVICE_FILE_MAX) + 1;
				// a random number of services for a service file

				// to ensure that any generated service file name is not repeated for the same
				// cloud
				HashMap sfUsed = new HashMap();

				int srvcFileNum = 0;
				while (srvcFileNum < Math.ceil(sf_num / 2.0D)) {
					Random rand2 = new Random();
					int serviceFile = rand2.nextInt(SERVICE_FILE_MAX) + 1;
					if (!sfUsed.containsKey(Integer.valueOf(serviceFile)))// the service file name is not found from
																	// before in the current cloud
					{
						String srvcFileName = (String) sfMap.get(Integer.valueOf(serviceFile));
						// a random service file name

						if (srvcFileName == null) {
							Random rand1 = new Random();
							int costOfAccess = rand1.nextInt(SERVICE_MAX) + 1;
							String srvcfile = String.format("%02d", new Object[] { Integer.valueOf(serviceFile) });
							String cost = String.format("%02d", new Object[] { Integer.valueOf(costOfAccess) });
							srvcFileName = new String(cost + "-f" + srvcfile);
							sfMap.put(Integer.valueOf(serviceFile), srvcFileName);
							// add this service file
						}

						if (srvcFileNum > 0)
							builder.append(",");
						builder.append(srvcFileName);//added cost of accessing service and service file name
						sfUsed.put(Integer.valueOf(serviceFile), srvcFileName);
						srvcFileNum++;
					}
				}
				builder.append("\r\n");
			}
			// finished writing a cloud and its service files and their services to config file

			bw.write(builder.toString());
			// write all clouds to config.txt
		} catch (IOException e) {
			e.printStackTrace();
			try {
				bw.close();
				fw.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		clouds = new HashMap();
		BufferedReader br = null;
		
		try {
			while (true) {
				// generate clouds, service files, services in files
				Generate(30, 35, 35);

				// if the hashMap has saved clouds before sleeping (all iterations in the while loop after the first)
				for (String key : clouds.keySet()) {
					clouds.get(key).terminate();
					clouds.get(key).join();
				}
				clouds.clear(); 

				//now, read the created config file and run the clouds threads
				br = new BufferedReader(new FileReader(FILENAME));
				String line;
				while ((line = br.readLine()) != null) { //for each line read from config file (i.e. cloud)
					String[] lineSplits = line.split(":");
					if (lineSplits.length == 3) {
						//create a new cloud thread, with its port number, services and all
						CloudServer cloudServer = new CloudServer(lineSplits[0],
								Integer.valueOf(Integer.parseInt(lineSplits[1])), lineSplits[2].split(","));
						cloudServer.start();
						clouds.put(lineSplits[0], cloudServer);
					}
				}

				Thread.sleep(INTERVAL);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
