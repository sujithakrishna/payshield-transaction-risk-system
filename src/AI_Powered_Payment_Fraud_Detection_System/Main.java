package AI_Powered_Payment_Fraud_Detection_System;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import Model.DashboardTransaction;
import Model.FraudRiskScore;
import Model.Payment;
import Model.TransactionReview;

import dao.DashboardDAO;
import dao.FraudRiskScoreDAO;
import dao.PaymentDAO;
import dao.TransactionReviewDAO;

import service.FraudDetectionService;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static PaymentDAO paymentDAO = new PaymentDAO();

    private static FraudRiskScoreDAO fraudRiskScoreDAO =
            new FraudRiskScoreDAO();

    private static TransactionReviewDAO transactionReviewDAO =
            new TransactionReviewDAO();

    private static DashboardDAO dashboardDAO =
            new DashboardDAO();

    private static FraudDetectionService fraudDetectionService =
            new FraudDetectionService();


    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        boolean running = true;

        System.out.println();
        System.out.println("==============================================");
        System.out.println("     AI POWERED PAYMENT FRAUD DETECTION");
        System.out.println("==============================================");


        while (running) {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("                 MAIN MENU");
            System.out.println("----------------------------------------------");

            System.out.println("1. User");
            System.out.println("2. Admin");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();


            switch (choice) {

                case "1":
                    userMenu();
                    break;

                case "2":
                    adminMenu();
                    break;

                case "0":
                    running = false;

                    System.out.println();
                    System.out.println(
                            "Thank you for using the Fraud Detection System.");

                    break;

                default:
                    System.out.println(
                            "Invalid choice.");
            }
        }


        scanner.close();
    }


    // =====================================================
    // USER MENU
    // =====================================================

    private static void userMenu() {

        boolean userRunning = true;


        while (userRunning) {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("                  USER MENU");
            System.out.println("----------------------------------------------");

            System.out.println("1. Create Transaction");
            System.out.println("2. My Transaction Status");
            System.out.println("0. Back");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();


            switch (choice) {

                case "1":
                    createTransaction();
                    break;

                case "2":
                    viewMyTransactionStatus();
                    break;

                case "0":
                    userRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice.");
            }
        }
    }


    // =====================================================
    // CREATE TRANSACTION
    // =====================================================

    private static void createTransaction() {

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("             CREATE TRANSACTION");
        System.out.println("----------------------------------------------");


        try {

            System.out.print("Enter Sender/User ID: ");

            int senderId =
                    Integer.parseInt(scanner.nextLine());


            System.out.print("Enter Receiver ID: ");

            int receiverId =
                    Integer.parseInt(scanner.nextLine());


            System.out.print("Enter Amount: ");

            BigDecimal amount =
                    new BigDecimal(scanner.nextLine());


            System.out.print("Enter Transaction Type: ");

            String transactionType =
                    scanner.nextLine();


            System.out.print("Enter Payment Method: ");

            String paymentMethod =
                    scanner.nextLine();


            System.out.print("Enter Location: ");

            String location =
                    scanner.nextLine();


            // =================================================
            // CREATE PAYMENT
            // =================================================

            Payment payment = new Payment();

            payment.setSenderId(senderId);

            payment.setReceiverId(receiverId);

            payment.setAmount(amount);

            payment.setTransactionType(transactionType);

            payment.setTransactionStatus("PENDING");

            payment.setPaymentMethod(paymentMethod);

            payment.setLocation(location);


            // =================================================
            // SAVE PAYMENT
            // =================================================

            paymentDAO.addPayment(payment);


            System.out.println();

            System.out.println(
                    "Transaction created successfully.");

            System.out.println(
                    "Transaction ID: "
                    + payment.getTransactionId());


            // =================================================
            // FRAUD ANALYSIS
            // =================================================

            System.out.println();

            System.out.println(
                    "Analyzing transaction for suspicious activity...");


            FraudRiskScore riskScore =
                    fraudDetectionService.analyzePayment(payment);


            // =================================================
            // SAVE RISK SCORE
            // =================================================

            fraudRiskScoreDAO.saveRiskScore(riskScore);


            // =================================================
            // DISPLAY FRAUD RESULT
            // =================================================

            System.out.println();

            System.out.println("----------------------------------------------");

            System.out.println(
                    "             FRAUD ANALYSIS RESULT");

            System.out.println("----------------------------------------------");


            System.out.println(
                    "Transaction ID : "
                    + riskScore.getTransactionId());

            System.out.println(
                    "Risk Score     : "
                    + riskScore.getRiskScore());

            System.out.println(
                    "Risk Level     : "
                    + riskScore.getRiskLevel());

            System.out.println(
                    "Risk Reason    : "
                    + riskScore.getRiskReason());

            System.out.println(
                    "Detection      : "
                    + riskScore.getDetectionMethod());


            // =================================================
            // MEDIUM / HIGH
            // =================================================

            if ("MEDIUM".equalsIgnoreCase(
                    riskScore.getRiskLevel())
                    ||
                    "HIGH".equalsIgnoreCase(
                            riskScore.getRiskLevel())) {

                handleUserExplanation(payment);

            } else {

                System.out.println();

                System.out.println(
                        "Transaction processed normally.");
            }


        } catch (NumberFormatException e) {

            System.out.println();

            System.out.println(
                    "Invalid number entered.");

        } catch (Exception e) {

            System.out.println();

            System.out.println(
                    "Unable to create transaction.");

            e.printStackTrace();
        }
    }


    // =====================================================
    // USER EXPLANATION
    // =====================================================

    private static void handleUserExplanation(
            Payment payment) {


        TransactionReview review =
                transactionReviewDAO
                        .getReviewByTransactionId(
                                payment.getTransactionId());


        if (review == null) {

            System.out.println();

            System.out.println(
                    "No review record found.");

            return;
        }


        System.out.println();

        System.out.println("----------------------------------------------");

        System.out.println(
                "          TRANSACTION REVIEW REQUIRED");

        System.out.println("----------------------------------------------");


        System.out.println(
                "Review ID      : "
                + review.getReviewId());

        System.out.println(
                "Transaction ID : "
                + review.getTransactionId());

        System.out.println(
                "Risk Score     : "
                + review.getRiskScore());

        System.out.println(
                "Risk Level     : "
                + review.getRiskLevel());

        System.out.println(
                "Reason         : "
                + review.getRiskReason());


        System.out.println();

        System.out.println(
                "This transaction requires additional review.");

        System.out.println(
                "Please explain why you made this transaction.");


        System.out.print(
                "Your explanation: ");


        String description =
                scanner.nextLine();


        while (description.trim().isEmpty()) {

            System.out.println(
                    "Explanation cannot be empty.");

            System.out.print(
                    "Please enter your explanation: ");

            description =
                    scanner.nextLine();
        }


        transactionReviewDAO.addUserDescription(
                review.getReviewId(),
                description);


        System.out.println();

        System.out.println(
                "User description added successfully.");

        System.out.println();

        System.out.println(
                "Your explanation has been submitted.");

        System.out.println(
                "Transaction is waiting for admin review.");
    }


    // =====================================================
    // USER - VIEW TRANSACTION STATUS
    // =====================================================

    private static void viewMyTransactionStatus() {

        System.out.println();

        System.out.println("----------------------------------------------");

        System.out.println(
                "          MY TRANSACTION STATUS");

        System.out.println("----------------------------------------------");


        try {

            System.out.print(
                    "Enter your User ID: ");

            int userId =
                    Integer.parseInt(
                            scanner.nextLine());


            System.out.print(
                    "Enter Transaction ID: ");

            long transactionId =
                    Long.parseLong(
                            scanner.nextLine());


            DashboardTransaction transaction =
                    dashboardDAO.getUserTransaction(
                            userId,
                            transactionId);


            // =================================================
            // NOT FOUND
            // =================================================

            if (transaction == null) {

                System.out.println();

                System.out.println(
                        "Transaction not found.");

                System.out.println(
                        "Please check your User ID and "
                        + "Transaction ID.");

                return;
            }


            // =================================================
            // TRANSACTION INFORMATION
            // =================================================

            System.out.println();

            System.out.println(
                    "==============================================");

            System.out.println(
                    "          TRANSACTION INFORMATION");

            System.out.println(
                    "==============================================");


            System.out.println(
                    "Transaction ID     : "
                    + transaction.getTransactionId());

            System.out.println(
                    "Receiver ID        : "
                    + transaction.getReceiverId());

            System.out.println(
                    "Amount             : "
                    + transaction.getAmount());

            System.out.println(
                    "Transaction Type   : "
                    + transaction.getTransactionType());

            System.out.println(
                    "Payment Method     : "
                    + transaction.getPaymentMethod());

            System.out.println(
                    "Location           : "
                    + transaction.getLocation());


            // =================================================
            // FRAUD ANALYSIS
            // =================================================

            System.out.println();

            System.out.println(
                    "---------------- FRAUD ANALYSIS ----------------");


            System.out.println(
                    "Risk Score         : "
                    + transaction.getRiskScore());

            System.out.println(
                    "Risk Level         : "
                    + transaction.getRiskLevel());

            System.out.println(
                    "Risk Reason        : "
                    + transaction.getRiskReason());

            System.out.println(
                    "Detection Method   : "
                    + transaction.getDetectionMethod());


            // =================================================
            // TRANSACTION STATUS
            // =================================================

            System.out.println();

            System.out.println(
                    "---------------- STATUS ----------------");


            System.out.println(
                    "Transaction Status : "
                    + transaction.getTransactionStatus());


            // =================================================
            // REVIEW INFORMATION
            // =================================================

            System.out.println();

            System.out.println(
                    "---------------- REVIEW ----------------");


            String reviewStatus =
                    transaction.getReviewStatus();


            if (reviewStatus == null) {

                System.out.println(
                        "Review Status      : NOT REQUIRED");

            } else {

                System.out.println(
                        "Review Status      : "
                        + reviewStatus);
            }


            System.out.println(
                    "User Explanation   : "
                    + transaction.getUserDescription());

            System.out.println(
                    "Admin Comment      : "
                    + transaction.getAdminComment());

            System.out.println(
                    "Reviewed At        : "
                    + transaction.getReviewedAt());


            // =================================================
            // USER-FRIENDLY MESSAGE
            // =================================================

            System.out.println();

            System.out.println(
                    "---------------- MESSAGE ----------------");


            String status =
                    transaction.getTransactionStatus();


            if ("SUCCESS".equalsIgnoreCase(status)) {

                if ("APPROVED".equalsIgnoreCase(
                        reviewStatus)) {

                    System.out.println(
                            "Your transaction was reviewed "
                            + "and approved by the admin.");

                } else {

                    System.out.println(
                            "Your transaction was processed "
                            + "successfully.");
                }


            } else if ("PENDING_REVIEW"
                    .equalsIgnoreCase(status)) {

                System.out.println(
                        "Your transaction is currently "
                        + "waiting for admin review.");

                System.out.println(
                        "Please wait for the final decision.");


            } else if ("REJECTED"
                    .equalsIgnoreCase(status)) {

                System.out.println(
                        "Your transaction was rejected "
                        + "by the admin.");

                if (transaction.getAdminComment() != null) {

                    System.out.println(
                            "Admin reason: "
                            + transaction.getAdminComment());
                }


            } else if ("PENDING"
                    .equalsIgnoreCase(status)) {

                System.out.println(
                        "Your transaction is still pending.");


            } else {

                System.out.println(
                        "Current transaction status: "
                        + status);
            }


            System.out.println();

            System.out.println(
                    "==============================================");


        } catch (NumberFormatException e) {

            System.out.println();

            System.out.println(
                    "Invalid User ID or Transaction ID.");

        } catch (Exception e) {

            System.out.println();

            System.out.println(
                    "Unable to retrieve transaction status.");

            e.printStackTrace();
        }
    }


    // =====================================================
    // ADMIN MENU
    // =====================================================

    private static void adminMenu() {

        boolean adminRunning = true;


        while (adminRunning) {

            System.out.println();

            System.out.println("----------------------------------------------");

            System.out.println(
                    "                 ADMIN MENU");

            System.out.println("----------------------------------------------");


            System.out.println("1. Dashboard");

            System.out.println("2. View Pending Reviews");

            System.out.println("3. Process Review");

            System.out.println("0. Back");


            System.out.print(
                    "Enter your choice: ");


            String choice =
                    scanner.nextLine();


            switch (choice) {

                case "1":
                    showDashboard();
                    break;

                case "2":
                    viewPendingReviews();
                    break;

                case "3":
                    processReview();
                    break;

                case "0":
                    adminRunning = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice.");
            }
        }
    }


    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    private static void showDashboard() {

        List<DashboardTransaction> transactions =
                dashboardDAO.getDashboardTransactions();


        System.out.println();

        System.out.println(
                "==================================================");

        System.out.println(
                "              PAYMENT FRAUD DASHBOARD");

        System.out.println(
                "==================================================");


        if (transactions.isEmpty()) {

            System.out.println();

            System.out.println(
                    "No transactions available.");

            return;
        }


        int totalTransactions =
                transactions.size();

        int successfulTransactions = 0;

        int pendingReviewTransactions = 0;

        int rejectedTransactions = 0;

        int pendingTransactions = 0;


        int lowRiskTransactions = 0;

        int mediumRiskTransactions = 0;

        int highRiskTransactions = 0;


        for (DashboardTransaction transaction :
                transactions) {


            String status =
                    transaction.getTransactionStatus();

            String riskLevel =
                    transaction.getRiskLevel();


            if ("SUCCESS".equalsIgnoreCase(status)) {

                successfulTransactions++;

            } else if ("PENDING_REVIEW"
                    .equalsIgnoreCase(status)) {

                pendingReviewTransactions++;

            } else if ("REJECTED"
                    .equalsIgnoreCase(status)) {

                rejectedTransactions++;

            } else if ("PENDING"
                    .equalsIgnoreCase(status)) {

                pendingTransactions++;
            }


            if ("LOW".equalsIgnoreCase(riskLevel)) {

                lowRiskTransactions++;

            } else if ("MEDIUM"
                    .equalsIgnoreCase(riskLevel)) {

                mediumRiskTransactions++;

            } else if ("HIGH"
                    .equalsIgnoreCase(riskLevel)) {

                highRiskTransactions++;
            }
        }


        // =================================================
        // SUMMARY
        // =================================================

        System.out.println();

        System.out.println(
                "Total Transactions     : "
                + totalTransactions);

        System.out.println(
                "Successful             : "
                + successfulTransactions);

        System.out.println(
                "Pending                : "
                + pendingTransactions);

        System.out.println(
                "Pending Review         : "
                + pendingReviewTransactions);

        System.out.println(
                "Rejected               : "
                + rejectedTransactions);


        System.out.println();

        System.out.println(
                "Low Risk               : "
                + lowRiskTransactions);

        System.out.println(
                "Medium Risk            : "
                + mediumRiskTransactions);

        System.out.println(
                "High Risk              : "
                + highRiskTransactions);


        // =================================================
        // RECENT TRANSACTIONS
        // =================================================

        System.out.println();

        System.out.println(
                "--------------------------------------------------");

        System.out.println(
                "                 RECENT TRANSACTIONS");

        System.out.println(
                "--------------------------------------------------");


        System.out.printf(
                "%-6s %-8s %-12s %-10s %-15s%n",
                "ID",
                "User",
                "Amount",
                "Risk",
                "Status");


        System.out.println(
                "--------------------------------------------------");


        for (DashboardTransaction transaction :
                transactions) {


            BigDecimal riskScore =
                    transaction.getRiskScore();


            String risk =
                    riskScore == null
                    ? "0"
                    : riskScore.toString();


            System.out.printf(
                    "%-6d %-8d %-12s %-10s %-15s%n",
                    transaction.getTransactionId(),
                    transaction.getSenderId(),
                    transaction.getAmount(),
                    risk,
                    transaction.getTransactionStatus());
        }


        System.out.println(
                "--------------------------------------------------");


        // =================================================
        // VIEW TRANSACTION
        // =================================================

        System.out.println();

        System.out.println(
                "Enter Transaction ID to view details.");

        System.out.println(
                "Enter 0 to return.");


        System.out.print(
                "Transaction ID: ");


        String input =
                scanner.nextLine();


        try {

            long transactionId =
                    Long.parseLong(input);


            if (transactionId == 0) {

                return;
            }


            DashboardTransaction selected =
                    null;


            for (DashboardTransaction transaction :
                    transactions) {


                if (transaction.getTransactionId()
                        == transactionId) {

                    selected =
                            transaction;

                    break;
                }
            }


            if (selected == null) {

                System.out.println();

                System.out.println(
                        "Transaction not found.");

                return;
            }


            displayTransactionDetails(
                    selected);


        } catch (NumberFormatException e) {

            System.out.println();

            System.out.println(
                    "Invalid transaction ID.");
        }
    }


    // =====================================================
    // DISPLAY TRANSACTION DETAILS
    // =====================================================

    private static void displayTransactionDetails(
            DashboardTransaction transaction) {


        System.out.println();

        System.out.println(
                "==================================================");

        System.out.println(
                "             TRANSACTION DETAILS");

        System.out.println(
                "==================================================");


        System.out.println(
                "Transaction ID  : "
                + transaction.getTransactionId());

        System.out.println(
                "Sender ID       : "
                + transaction.getSenderId());

        System.out.println(
                "Receiver ID     : "
                + transaction.getReceiverId());

        System.out.println(
                "Amount          : "
                + transaction.getAmount());

        System.out.println(
                "Transaction Type: "
                + transaction.getTransactionType());

        System.out.println(
                "Payment Method  : "
                + transaction.getPaymentMethod());

        System.out.println(
                "Location        : "
                + transaction.getLocation());

        System.out.println(
                "Transaction Time: "
                + transaction.getTransactionTime());


        System.out.println();

        System.out.println(
                "---------------- FRAUD ANALYSIS ----------------");


        System.out.println(
                "Risk Score      : "
                + transaction.getRiskScore());

        System.out.println(
                "Risk Level      : "
                + transaction.getRiskLevel());

        System.out.println(
                "Risk Reason     : "
                + transaction.getRiskReason());

        System.out.println(
                "Detection Method: "
                + transaction.getDetectionMethod());


        System.out.println();

        System.out.println(
                "---------------- TRANSACTION ----------------");


        System.out.println(
                "Transaction Status: "
                + transaction.getTransactionStatus());


        System.out.println();

        System.out.println(
                "---------------- REVIEW ----------------");


        System.out.println(
                "Review Status   : "
                + transaction.getReviewStatus());

        System.out.println(
                "User Explanation: "
                + transaction.getUserDescription());

        System.out.println(
                "Admin Comment   : "
                + transaction.getAdminComment());

        System.out.println(
                "Reviewed At     : "
                + transaction.getReviewedAt());


        System.out.println();

        System.out.println(
                "==================================================");
    }


    // =====================================================
    // VIEW PENDING REVIEWS
    // =====================================================

    private static void viewPendingReviews() {

        System.out.println();

        System.out.println(
                "----------------------------------------------");

        System.out.println(
                "             PENDING REVIEWS");

        System.out.println(
                "----------------------------------------------");


        List<TransactionReview> pendingReviews =
                transactionReviewDAO
                        .getPendingReviews();


        if (pendingReviews.isEmpty()) {

            System.out.println(
                    "There are no pending reviews.");

            return;
        }


        System.out.println(
                "Pending Reviews: "
                + pendingReviews.size());


        for (TransactionReview review :
                pendingReviews) {


            System.out.println();

            System.out.println(
                    "----------------------------------------------");


            System.out.println(
                    "Review ID       : "
                    + review.getReviewId());

            System.out.println(
                    "Transaction ID  : "
                    + review.getTransactionId());

            System.out.println(
                    "User ID         : "
                    + review.getUserId());

            System.out.println(
                    "Risk Score      : "
                    + review.getRiskScore());

            System.out.println(
                    "Risk Level      : "
                    + review.getRiskLevel());

            System.out.println(
                    "Risk Reason     : "
                    + review.getRiskReason());

            System.out.println(
                    "User Explanation: "
                    + review.getUserDescription());

            System.out.println(
                    "Review Status   : "
                    + review.getReviewStatus());

            System.out.println(
                    "----------------------------------------------");
        }
    }


    // =====================================================
    // PROCESS REVIEW
    // =====================================================

    private static void processReview() {

        List<TransactionReview> pendingReviews =
                transactionReviewDAO
                        .getPendingReviews();


        if (pendingReviews.isEmpty()) {

            System.out.println();

            System.out.println(
                    "There are no pending reviews.");

            return;
        }


        System.out.println();

        System.out.println(
                "----------------------------------------------");

        System.out.println(
                "                PROCESS REVIEW");

        System.out.println(
                "----------------------------------------------");


        for (TransactionReview review :
                pendingReviews) {


            System.out.println();

            System.out.println(
                    "Review ID: "
                    + review.getReviewId()
                    + " | Transaction ID: "
                    + review.getTransactionId()
                    + " | Risk: "
                    + review.getRiskLevel());
        }


        System.out.println();

        System.out.print(
                "Enter Review ID to process (0 to cancel): ");


        long reviewId;


        try {

            reviewId =
                    Long.parseLong(
                            scanner.nextLine());

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid Review ID.");

            return;
        }


        if (reviewId == 0) {

            return;
        }


        // =================================================
        // FIND REVIEW
        // =================================================

        TransactionReview selectedReview =
                null;


        for (TransactionReview review :
                pendingReviews) {


            if (review.getReviewId()
                    == reviewId) {

                selectedReview =
                        review;

                break;
            }
        }


        if (selectedReview == null) {

            System.out.println(
                    "Review ID not found.");

            return;
        }


        // =================================================
        // DISPLAY REVIEW
        // =================================================

        System.out.println();

        System.out.println(
                "----------------------------------------------");

        System.out.println(
                "           SELECTED TRANSACTION");

        System.out.println(
                "----------------------------------------------");


        System.out.println(
                "Review ID       : "
                + selectedReview.getReviewId());

        System.out.println(
                "Transaction ID  : "
                + selectedReview.getTransactionId());

        System.out.println(
                "User ID         : "
                + selectedReview.getUserId());

        System.out.println(
                "Risk Score      : "
                + selectedReview.getRiskScore());

        System.out.println(
                "Risk Level      : "
                + selectedReview.getRiskLevel());

        System.out.println(
                "Risk Reason     : "
                + selectedReview.getRiskReason());

        System.out.println(
                "User Explanation: "
                + selectedReview.getUserDescription());

        System.out.println(
                "Review Status   : "
                + selectedReview.getReviewStatus());


        // =================================================
        // ADMIN DECISION
        // =================================================

        System.out.println();

        System.out.println(
                "1. Approve Transaction");

        System.out.println(
                "2. Reject Transaction");

        System.out.println(
                "0. Cancel");


        System.out.print(
                "Enter your choice: ");


        String choice =
                scanner.nextLine();


        // =================================================
        // APPROVE
        // =================================================

        if (choice.equals("1")) {

            System.out.println();

            System.out.print(
                    "Enter admin comment: ");


            String adminComment =
                    scanner.nextLine();


            if (adminComment.trim().isEmpty()) {

                adminComment =
                        "Transaction approved after review.";
            }


            transactionReviewDAO
                    .approveTransaction(
                            selectedReview.getReviewId(),
                            selectedReview.getTransactionId(),
                            adminComment);
        }


        // =================================================
        // REJECT
        // =================================================

        else if (choice.equals("2")) {

            System.out.println();

            System.out.print(
                    "Enter admin comment: ");


            String adminComment =
                    scanner.nextLine();


            if (adminComment.trim().isEmpty()) {

                adminComment =
                        "Transaction rejected after review.";
            }


            transactionReviewDAO
                    .rejectTransaction(
                            selectedReview.getReviewId(),
                            selectedReview.getTransactionId(),
                            adminComment);
        }


        else if (choice.equals("0")) {

            System.out.println(
                    "Review cancelled.");
        }


        else {

            System.out.println(
                    "Invalid choice.");
        }
    }
}