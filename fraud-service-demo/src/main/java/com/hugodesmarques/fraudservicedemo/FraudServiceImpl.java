package com.hugodesmarques.fraudservicedemo;

import com.hugodesmarques.fraudservice.CountriesRequest;
import com.hugodesmarques.fraudservice.CountriesResponse;
import com.hugodesmarques.fraudservice.Country;
import com.hugodesmarques.fraudservice.FraudRiskRequest;
import com.hugodesmarques.fraudservice.FraudRiskResponse;
import com.hugodesmarques.fraudservice.FraudServiceGrpc;
import com.hugodesmarques.fraudservice.RiskAssessment;
import com.hugodesmarques.fraudservice.RiskLevel;
import com.hugodesmarques.fraudservice.Transaction;
import com.hugodesmarques.fraudservice.TransactionType;
import com.hugodesmarques.fraudservice.TransactionsByCountryRequest;
import com.hugodesmarques.fraudservice.TransactionsResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@GrpcService
public class FraudServiceImpl extends FraudServiceGrpc.FraudServiceImplBase {

    private static final Map<String, List<Transaction>> COUNTRY_TRANSACTIONS = new HashMap<>();
    private static final List<Country> COUNTRIES = new ArrayList<>();

    // Initialize sample data
    static {
        // Initialize countries
        initializeCountries();

        // Initialize transactions
        initializeTransactions();
    }

    private static void initializeCountries() {
        COUNTRIES.add(Country.newBuilder().setCode("US").setName("United States").build());
        COUNTRIES.add(Country.newBuilder().setCode("UK").setName("United Kingdom").build());
        COUNTRIES.add(Country.newBuilder().setCode("CA").setName("Canada").build());
        COUNTRIES.add(Country.newBuilder().setCode("DE").setName("Germany").build());
        COUNTRIES.add(Country.newBuilder().setCode("JP").setName("Japan").build());
    }

    private static void initializeTransactions() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // US Transactions
        List<Transaction> usTransactions = new ArrayList<>();
        usTransactions.add(Transaction.newBuilder()
                .setId("TX-US-001")
                .setAmount(299.99)
                .setCurrency("USD")
                .setMerchant("Amazon")
                .setTransactionDate(LocalDateTime.now().minusDays(1).format(formatter))
                .setCardNumberLastFour("4321")
                .setType(TransactionType.PURCHASE)
                .build());
        usTransactions.add(Transaction.newBuilder()
                .setId("TX-US-002")
                .setAmount(1500.00)
                .setCurrency("USD")
                .setMerchant("Apple Store")
                .setTransactionDate(LocalDateTime.now().minusDays(2).format(formatter))
                .setCardNumberLastFour("5678")
                .setType(TransactionType.PURCHASE)
                .build());
        COUNTRY_TRANSACTIONS.put("US", usTransactions);

        // UK Transactions
        List<Transaction> ukTransactions = new ArrayList<>();
        ukTransactions.add(Transaction.newBuilder()
                .setId("TX-UK-001")
                .setAmount(120.50)
                .setCurrency("GBP")
                .setMerchant("Tesco")
                .setTransactionDate(LocalDateTime.now().minusHours(12).format(formatter))
                .setCardNumberLastFour("9876")
                .setType(TransactionType.PURCHASE)
                .build());
        COUNTRY_TRANSACTIONS.put("UK", ukTransactions);

        // Add more transactions for other countries
        // CA, DE, JP, etc.
    }

    @Override
    public void getCountries(CountriesRequest request, StreamObserver<CountriesResponse> responseObserver) {
        CountriesResponse response = CountriesResponse.newBuilder()
                .addAllCountries(COUNTRIES)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTransactionsByCountry(TransactionsByCountryRequest request, StreamObserver<TransactionsResponse> responseObserver) {
        String countryCode = request.getCountryCode();
        List<Transaction> transactions = COUNTRY_TRANSACTIONS.getOrDefault(countryCode, Collections.emptyList());

        TransactionsResponse response = TransactionsResponse.newBuilder()
                .addAllTransactions(transactions)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void analyzeFraudRisk(FraudRiskRequest request, StreamObserver<FraudRiskResponse> responseObserver) {
        Country country = request.getCountry();
        List<Transaction> transactions = request.getTransactionsList();

        List<RiskAssessment> riskAssessments = transactions.stream()
                .map(transaction -> analyzeTransaction(country, transaction))
                .collect(Collectors.toList());

        FraudRiskResponse response = FraudRiskResponse.newBuilder()
                .addAllRiskAssessments(riskAssessments)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private RiskAssessment analyzeTransaction(Country country, Transaction transaction) {
        // Implement fraud risk analysis logic
        List<String> riskFactors = new ArrayList<>();
        double riskScore = 0.0;

        // Check for high amount transactions
        if (transaction.getAmount() > 1000.0) {
            riskFactors.add("High transaction amount");
            riskScore += 25.0;
        }

        // Check for cross-border transactions (simplified example)
        if (!transaction.getCurrency().equals("USD") && country.getCode().equals("US")) {
            riskFactors.add("Cross-border transaction");
            riskScore += 15.0;
        }

        // Check for unusual merchants
        if (transaction.getMerchant().toLowerCase().contains("unknown")) {
            riskFactors.add("Unusual merchant");
            riskScore += 30.0;
        }

        // Determine risk level based on risk score
        RiskLevel riskLevel;
        if (riskScore < 20.0) {
            riskLevel = RiskLevel.LOW;
        } else if (riskScore < 40.0) {
            riskLevel = RiskLevel.MEDIUM;
        } else if (riskScore < 70.0) {
            riskLevel = RiskLevel.HIGH;
        } else {
            riskLevel = RiskLevel.CRITICAL;
        }

        return RiskAssessment.newBuilder()
                .setTransactionId(transaction.getId())
                .setRiskLevel(riskLevel)
                .addAllRiskFactors(riskFactors)
                .setRiskScore(riskScore)
                .build();
    }
}