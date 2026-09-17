package service;

import java.math.BigDecimal;

import Model.FraudAlert;
import Model.FraudRiskScore;
import Model.Payment;
import Model.TransactionReview;
import dao.FraudAlertDAO;
import dao.PaymentDAO;
import dao.TransactionReviewDAO;

public class FraudDetectionService {

    private PaymentDAO paymentDAO;
    private FraudAlertDAO fraudAlertDAO;
    private TransactionReviewDAO transactionReviewDAO;

    public FraudDetectionService() {
        paymentDAO = new PaymentDAO();
        fraudAlertDAO = new FraudAlertDAO();
        transactionReviewDAO = new TransactionReviewDAO();
    }


    public FraudRiskScore analyzePayment(Payment payment) {

        BigDecimal riskScore = BigDecimal.ZERO;

        StringBuilder riskReason = new StringBuilder();


        // Rule 1: High Transaction Amount
        if (payment.getAmount().compareTo(new BigDecimal("10000")) > 0) {

            riskScore = riskScore.add(new BigDecimal("40"));

            riskReason.append("High transaction amount; ");
        }


        // Rule 2: High Transaction Frequency
        int previousTransactions =
                paymentDAO.countPreviousTransactions(
                        payment.getSenderId(),
                        payment.getTransactionId());

        if (previousTransactions > 3) {

            riskScore = riskScore.add(new BigDecimal("30"));

            riskReason.append(
                    "High transaction frequency: "
                    + previousTransactions
                    + " previous transaction(s) within 10 minutes; ");
        }


        // Rule 3: Duplicate Transaction
        int duplicateTransactions =
                paymentDAO.countDuplicateTransactions(payment);

        if (duplicateTransactions > 0) {

            riskScore = riskScore.add(new BigDecimal("20"));

            riskReason.append(
                    "Duplicate transaction: "
                    + duplicateTransactions
                    + " similar transaction(s) within 5 minutes; ");
        }


        // Rule 4: Different Location
        int differentLocationTransactions =
                paymentDAO.countDifferentLocationTransactions(payment);

        if (differentLocationTransactions > 0) {

            riskScore = riskScore.add(new BigDecimal("30"));

            riskReason.append(
                    "Transaction from different location: "
                    + differentLocationTransactions
                    + " previous transaction(s) from another location within 10 minutes; ");
        }


        // Determine Risk Level
        String riskLevel;

        if (riskScore.compareTo(new BigDecimal("70")) >= 0) {

            riskLevel = "HIGH";

        } else if (riskScore.compareTo(new BigDecimal("30")) >= 0) {

            riskLevel = "MEDIUM";

        } else {

            riskLevel = "LOW";
        }


        // No suspicious activity
        if (riskReason.length() == 0) {

            riskReason.append("No suspicious activity detected");
        }


        // Create Fraud Risk Score
        FraudRiskScore fraudRiskScore = new FraudRiskScore();

        fraudRiskScore.setTransactionId(payment.getTransactionId());
        fraudRiskScore.setRiskScore(riskScore);
        fraudRiskScore.setRiskLevel(riskLevel);
        fraudRiskScore.setRiskReason(riskReason.toString());
        fraudRiskScore.setDetectionMethod("RULE_BASED");


        // LOW RISK
        if (riskLevel.equals("LOW")) {

            transactionReviewDAO.updateTransactionStatus(
                    payment.getTransactionId(),
                    "SUCCESS");

            System.out.println();
            System.out.println("Risk Level: LOW");
            System.out.println("Transaction Status: SUCCESS");
            System.out.println("Transaction processed normally.");
        }


        // MEDIUM / HIGH RISK
        else {

            // Transaction waits for review
            transactionReviewDAO.updateTransactionStatus(
                    payment.getTransactionId(),
                    "PENDING_REVIEW");


            // Create review record
            TransactionReview review = new TransactionReview();

            review.setTransactionId(payment.getTransactionId());
            review.setUserId(payment.getSenderId());
            review.setRiskScore(riskScore);
            review.setRiskLevel(riskLevel);
            review.setRiskReason(riskReason.toString());
            review.setReviewStatus("PENDING");

            transactionReviewDAO.createReview(review);


            System.out.println();
            System.out.println("Risk Level: " + riskLevel);
            System.out.println("Transaction Status: PENDING_REVIEW");
            System.out.println("User explanation is required.");
            System.out.println("Transaction sent for admin review.");


            // HIGH risk also creates a fraud alert
            if (riskLevel.equals("HIGH")) {

                FraudAlert alert = new FraudAlert();

                alert.setTransactionId(payment.getTransactionId());
                alert.setRiskScore(riskScore);
                alert.setAlertReason(riskReason.toString());
                alert.setAlertStatus("OPEN");

                fraudAlertDAO.createAlert(alert);

                System.out.println("HIGH risk fraud alert created.");
            }
        }


        return fraudRiskScore;
    }
}