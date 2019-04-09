This is the client side receiving requests from the user .

It is responsible for looking up for the user request in the availble clouds in the multi cloud environment, creating a composed service containing all services fullfilling a user request and returning them to the user. Also, the time taken, number of clouds combined and services accessed, are written to analytics.csv for analysing the performance of the algorithm
