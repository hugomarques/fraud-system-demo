package com.hugodesmarques.fraudclient;

import com.hugodesmarques.fraudservice.CountriesRequest;
import com.hugodesmarques.fraudservice.CountriesResponse;
import com.hugodesmarques.fraudservice.Country;
import com.hugodesmarques.fraudservice.FraudRiskRequest;
import com.hugodesmarques.fraudservice.FraudRiskResponse;
import com.hugodesmarques.fraudservice.FraudServiceGrpc;
import com.hugodesmarques.fraudservice.RiskAssessment;
import com.hugodesmarques.fraudservice.Transaction;
import com.hugodesmarques.fraudservice.TransactionsByCountryRequest;
import com.hugodesmarques.fraudservice.TransactionsResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudServiceProxy {

    @GrpcClient("fraudService")
    private FraudServiceGrpc.FraudServiceBlockingStub fraudServiceStub;

    public List<Country> getCountries() {
        CountriesRequest request = CountriesRequest.newBuilder().build();
        CountriesResponse response = fraudServiceStub.getCountries(request);
        return response.getCountriesList();
    }

    public List<Transaction> getTransactionsByCountry(String countryCode) {
        TransactionsByCountryRequest request = TransactionsByCountryRequest.newBuilder()
                .setCountryCode(countryCode)
                .build();
        TransactionsResponse response = fraudServiceStub.getTransactionsByCountry(request);
        return response.getTransactionsList();
    }

    public List<RiskAssessment> analyzeFraudRisk(Country country, List<Transaction> transactions) {
        FraudRiskRequest request = FraudRiskRequest.newBuilder()
                .setCountry(country)
                .addAllTransactions(transactions)
                .build();
        FraudRiskResponse response = fraudServiceStub.analyzeFraudRisk(request);
        return response.getRiskAssessmentsList();
    }
}
