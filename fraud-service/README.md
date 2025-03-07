# Fraud Detection Service

The **Fraud Detection Service** is a gRPC-based service that provides fraud-related transaction data.

## 📂 Folder Structure

```
/fraud-service
  /src
    /main
      /java
        /com/hugodesmarques/fraudservice  # Java code
      /resources
        application.yml  # Configuration
      /proto  # Protocol Buffers definitions
        fraud_service.proto
  build.gradle
  settings.gradle
  README.md
```

## 🛠️ Setup & Running

### **1️⃣ Install Dependencies**
Make sure you have Java 21+ and Gradle installed.

### **2️⃣ Build the Project**
Run:
```shell
    ./gradlew build
```

### **3️⃣ Start the Service**
Run:
```shell
    ./gradlew bootRun
```

The service should start on `localhost:9090`.

## 📡 API Endpoints (gRPC)

Once the service is running, you can test it using `grpcurl`.

```shell
# Install grpcurl
# On Mac: brew install grpcurl
# On Linux: Follow instructions at https://github.com/fullstorydev/grpcurl

# List all available services (assuming server reflection is enabled)
grpcurl -plaintext localhost:9090 list

# List methods in a service
grpcurl -plaintext localhost:9090 list fraudservice.FraudService

# Call a method
grpcurl -plaintext -d '{}' localhost:9090 fraudservice.FraudService/GetCountries

# Call a method with parameters
grpcurl -plaintext -d '{"country_code": "US"}' localhost:9090 fraudservice.FraudService/GetTransactionsByCountry
```

## 📝 Proto File (`fraud_service.proto`)

The service is defined using Protocol Buffers: `src/main/proto`.

## 🛠️ Logging & Debugging

- Application logs can be found in the console output.
- Use `grpcurl` to test API responses.
