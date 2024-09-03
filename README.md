# Players Microservice

### Description

A RESTful microservice that provides player information through HTTP GET requests. The data is initialized from a CSV file, and saved to PostgreSQL but can be adapted to load from different sources or persist the data to different databases in the future.

### How to Use

#### Example Requests:

- **Get all players:**

  ```bash
  GET http://localhost:8080/players
  ```

- **Get a player by ID:**
  ```bash
  GET http://localhost:8080/players/{playerID}
  ```
  Example:
  ```bash
  GET http://localhost:8080/players/P1
  ```

#### Example Responses:

- **Get all players:**

  ```json
  [
    {
      "playerID": "P1",
      "nameFirst": "John",
      "nameLast": "Doe",
      "bats": "R",
      "throwingHand": "R"
    }
  ]
  ```

- **Get a player by ID:**
  ```json
  {
    "playerID": "P1",
    "nameFirst": "John",
    "nameLast": "Doe",
    "bats": "R",
    "throwingHand": "R"
  }
  ```

### Assumptions

- **Abstract Service:** The service is built to allow flexibility in the data initialization process. Currently, it loads data from a CSV file, but the architecture allows for future modifications to load from different sources or accept data via REST POST requests.
- **CSV Parser:** The parser is designed based on the assumption that corrupted or invalid data in the CSV file should not cause the service to fail. Instead, invalid entries are ignored, and only valid data is used.
- **Validation Strategy:** Validations are split between the CSV parser and the entity class. The parser handles structural and type validations, while the entity uses annotations to enforce logical validations, such as ensuring dates are in the past or present.
- **Customizability:** The current validations are based on my assumptions, but they can be easily modified according to specific needs.

### Scaling the Service

To scale this service effectively I would consider:

- **Concurrency and Asynchronous Processing:** Adding asynchronous request handling to manage tasks, such as data loading or processing, without blocking incoming requests.

- **Horizontal Scaling:** increasing the number of instances of this service running in parallel, to handle more traffic and improve availability.

- **Replicas and Sharding:** To handle a larger volume of data, I'd consider using a different DB like Cassandra or MongoDB, and sharding the database. 

- **Caching:** Implementing a caching mechanism (such as Redis) to store frequently accessed data in memory, reducing the load on the database and improving response times.

- **Monitoring and Alerts:** Setting up monitoring and alerts to keep track of service performance and error rates. Tools like Prometheus and Grafana can be used for real-time monitoring and alerting.

- **Load Balancing:** Use a load balancer to distribute incoming requests evenly across multiple instances of the service, to ensure high availability and reliability.

