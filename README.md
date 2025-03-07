# Fraud System Demo

This repository contains two related projects:

1. **fraud-service-demo** - A gRPC-based service that provides fraud detection data.
2. **fraud-client** - A client that interacts with the fraud service.

## 📂 Project Structure

```
/fraud-system
  /fraud-service  # gRPC service
    build.gradle
    settings.gradle
    Dockerfile
    src/
  /fraud-client   # Client application
    build.gradle
    settings.gradle
    Dockerfile
    src/
  README.md # This file
  docker-compose.yml # Docker Compose file
```

## 🏗️ How to Build

Each project can be built independently. Navigate to the respective folder and run:

```shell
    ./gradlew clean build
```


For Windows:

```shell
    gradlew.bat build
```
   

## 🚀 Running the Services

Each service can be started independently. Navigate to `fraud-service-demo` and run:

    ./gradlew bootRun

To start the client, navigate to `fraud-client` and run:

    ./gradlew bootRun

## 🛠️ Testing with `grpcurl`

Once the service is running on `localhost:9090`, you can test it using `grpcurl`. See the `fraud-service/README.md` for details.

---

# Fraud Detection Service

The **Fraud Detection Service** is a gRPC-based service that provides fraud-related transaction data.

## 📂 Folder Structure

/fraud-service
/src
/main
/java/com/example/fraudservice  # Java code
/resources
application.yml  # Configuration
/proto  # Protocol Buffers definitions
fraud_service.proto
build.gradle
settings.gradle
README.md

## 🛠️ Setup & Running

### **1️⃣ Install Dependencies**
Make sure you have Java 17+ and Gradle installed.

### **2️⃣ Build the Project**
Run:

    ./gradlew build

### **3️⃣ Start the Service**
Run:

    ./gradlew bootRun

The service should start on `localhost:9090`.

## 📡 API Endpoints (gRPC)

Once the service is running, you can test it using `grpcurl`.

### **List all available services**

    grpcurl -plaintext localhost:9090 list

### **List methods in a service**

    grpcurl -plaintext localhost:9090 list fraudservice.FraudService

### **Call a method (Get list of countries)**

    grpcurl -plaintext -d '{}' localhost:9090 fraudservice.FraudService/GetCountries

### **Call a method with parameters (Get transactions by country)**

    grpcurl -plaintext -d '{"country_code": "US"}' localhost:9090 fraudservice.FraudService/GetTransactionsByCountry

## 📝 Proto File (`fraud_service.proto`)

The service is defined using Protocol Buffers:

    syntax = "proto3";

    package fraudservice;

    service FraudService {
        rpc GetCountries (Empty) returns (CountryList);
        rpc GetTransactionsByCountry (TransactionRequest) returns (TransactionList);
    }

    message Empty {}

    message CountryList {
        repeated string countries = 1;
    }

    message TransactionRequest {
        string country_code = 1;
    }

    message TransactionList {
        repeated Transaction transactions = 1;
    }

    message Transaction {
        string transaction_id = 1;
        double amount = 2;
        string timestamp = 3;
    }

## 🛠️ Logging & Debugging

- Application logs can be found in the console output.
- Use `grpcurl` to test API responses.

## 🏗️ Future Improvements

- Add authentication to gRPC calls.
- Implement a database to store transactions.
- Optimize performance for high-load scenarios.
