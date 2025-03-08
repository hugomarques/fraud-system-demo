package com.hugodesmarques.fraudclient;

import java.util.List;

import com.hugodesmarques.fraudservice.Country;
import com.hugodesmarques.fraudservice.RiskAssessment;
import com.hugodesmarques.fraudservice.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FraudClientApplication implements CommandLineRunner {

    @Autowired
    private FraudServiceProxy fraudServiceProxy;

    public static void main(String[] args) {
        SpringApplication.run(FraudClientApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        final List<Country> countries = fraudServiceProxy.getCountries();
        for (Country country : countries) {
            System.out.println("Country: " + country.getName());
            final List<Transaction> transactions = fraudServiceProxy.getTransactionsByCountry(country.getCode());
            final List<RiskAssessment> riskAssessments = fraudServiceProxy.analyzeFraudRisk(country, transactions);
            for (RiskAssessment riskAssessment : riskAssessments) {
                System.out.println("Risk assessment: " + riskAssessment.getRiskLevel() + " - " + riskAssessment.getRiskScore());
            }
        }
    }
}
