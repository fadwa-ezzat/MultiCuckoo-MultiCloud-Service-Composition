# MultiCloud-Service-Composition
Our MCE simulator is implemented in a Java-based tool that uses client/server programming based on sockets to model the interactions in an MCE at different scales.

MultiCuckoo.zip represents the client side, where the MultiCuckoo algorithm was developed and any other algorithms can be developed and tested. The server side should be running first. The user request is a set of service files (e.g. f03 f06 f18) that are space separated and input from the console.

CloudSim.zip represents the server side where multi clouds are periodically generated and so is their service files and services. It should be running first before running the client side.

The main components of the simulator are:
• Generator: The generator tool is responsible for the production of the configuration file, which stores the number of clouds in the MCE, their properties, and the available services.
• Configuration editor: The configuration editor is a thread that periodically changes the configuration file to reflect the dynamic nature of the cloud environment.
• Emulator: The emulator reads the configuration file and uses threading to produce the clouds needed on their corresponding IP address and port. The produced clouds are accessed by the service client through a TCP connection that retrieves a list of the cloud’s services or calls a specific service in order to view its contents.
• Service client: The service client is a library used for service requests by our MultiCuckoo algorithm and any other algorithms to be implemented.

Anyone is welcome to use the code and test his own MultiCloud Service composition algorithm on this simulator.
