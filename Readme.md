# URL Monitor

URL Monitor is a robust Spring Boot application designed to monitor and record the status of various web endpoints. It periodically checks the specified URLs and stores the results, including HTTP status codes and response times, helping users maintain insights into the uptime and performance of their web services.

## Architecture Overview

The application follows a microservices architecture pattern, with a focus on decoupling the core functionalities into separate components:

- **API Layer**: Handles incoming requests and routes them to appropriate service layers.
- **Service Layer**: Contains business logic for monitoring URLs and managing data.
- **Data Access Layer**: Interacts with the database to store and retrieve monitoring results.
- **Database**: Uses MySQL for persistent storage of monitoring data.
- **Distribute Locking**: Uses Redis for manage distributed locks to ensure data consistency across multiple instances.

### Project Structure

```plaintext
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── bahmet
│   │   │           └── urlmonitor
│   │   │               ├── controller      # REST API controllers
│   │   │               │   └── filter      # Filters
│   │   │               ├── service         # Business logic
│   │   │               ├── repository      # Data access repositories
│   │   │               │   └── model       # Entity definitions
│   │   │               ├── dto             # Data transfer objects
│   │   │               ├── exception       # Custom exceptions
│   │   │               ├── util            # Utility classes
│   │   │               │   └── mapper      # Model mapper configurations
│   │   │               └── configuration   # Configuration classes
│   │   └── resources
│   │       ├── db
│   │       │   └── migration               # Flyway database migrations
│   │       └── application.yml             # Application configurations
│   └── test                                # Integration tests
├── Dockerfile                              # Docker configuration file
├── docker-compose.yml                      # Docker-compose setup
└── README.md
```

### Main Technologies
- **Spring Boot**: Framework for building stand-alone, production-grade Spring based Applications.
- **MySQL**: Relational database management system.
- **Redis**: In-memory data structure store, used as a database.
- **Flyway**: Tool for database migrations.
- **Docker**: Platform for developing, shipping, and running applications.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 17
- Docker
- Docker Compose

### Running the Application

1. **Clone the repository**
```
git clone https://github.com/yourgithub/url-monitor.git
cd url-monitor
```

2. **Build the application**
```
mvn clean package
```

3. **Start the services**
```
docker-compose up --build
```

### Accessing the Application
Once the application is running, it can be accessed via:
```
http://localhost:8080/
```

### Testing with Postman
I have provided a collection of Postman requests to help you interact with and test the API. This collection can be found in the Postman folder of the project.

To use it:
- Open Postman.
- Import the collection by navigating to the Postman folder and selecting the collection file.
- Ensure your local services are running as described above.
- Send requests from Postman to test various endpoints.

### API Documentation
API documentation is provided via Swagger and can be accessed through the following URL when the application is running:
```
http://localhost:8080/swagger-ui.html
```
