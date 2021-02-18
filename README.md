# MultiCloud-Service-Composition
Our MCE simulator is implemented in a Java-based tool that uses client/server programming based on sockets to model the interactions in an MCE at different scales.

**CloudSim.zip** represents the server side where multi clouds are periodically generated and so is their service files and services. It should be running first before running the client side.

**MultiCuckoo.zip** represents the client side, where the MultiCuckoo algorithm was developed and any other algorithms can be developed and tested. The server side should be running first. The user request is a set of service files (e.g. f03 f06 f18) that are space separated and input from the console.

The main components of the simulator are:

**• Generator:** The generator tool is responsible for the production of the configuration file, which stores the number of clouds in the MCE, their properties, and the available services.

**• Configuration editor:** The configuration editor is a thread that periodically changes the configuration file to reflect the dynamic nature of the cloud environment.

**• Emulator:** The emulator reads the configuration file and uses threading to produce the clouds needed on their corresponding IP address and port. The produced clouds are accessed by the service client through a TCP connection that retrieves a list of the cloud’s services or calls a specific service in order to view its contents.

**• Service client:** The service client is a library used for service requests by our MultiCuckoo algorithm and any other algorithms to be implemented.

Anyone is welcome to use the code and test his own MultiCloud Service composition algorithm on this simulator.

# CloudSim
- Performance measures are the number of examined services (checked whether to add to the result or not)
and if they were useful (should be added to result), their cloud is added to the number of combined clouds

•	This project is responsible for the dynamic generation of MCE which contains clouds and their services.

•	The main class is GenerateServicesConfig.java

•	The simulation of the access time can also be changed (line 26).

•	At the beginning of the class, we have values reflecting the maximum number of clouds and services, which can be changed. In my environment, the services are file names (e.g. f01, f04, f12, etc.) and each file having a random number of services, e.g. if f01 has 5 services, it will be written as 05-f01. The service files and their services are created in the main class at lines 81-86. This means that a user’s request to this environment will be just the file name, i.e. if a user needs services 1,3,5, he will ask for f01 f03 f05.

•	To simulate a MCE, change the parameters of line 128 to reflect the number of clouds and services in your environment. The created clouds are saved to a file in the user directory folder config.txt (line 52).

•	The simulated clouds are run (as they are Threads) and saved to a hashmap so as to be readily available when accesses/needed (line 143,144).

•	Run the main class; the console shows the number of clouds created and their ports, etc. (if you open the configuration file, you will also find them). If you monitor the console, you will find that the values will change after some time, simulating a dynamic environment (and so will the configuration file’s values).
