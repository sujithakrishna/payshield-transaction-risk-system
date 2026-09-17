package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Model.DashboardTransaction;
import util.DBConnection;

public class DashboardDAO {


    // =====================================================
    // GET ALL TRANSACTIONS FOR DASHBOARD
    // =====================================================

    public List<DashboardTransaction> getDashboardTransactions() {

        List<DashboardTransaction> transactions =
                new ArrayList<>();


        String sql =
                "SELECT " +
                "t.transaction_id, " +
                "t.sender_id, " +
                "t.receiver_id, " +
                "t.amount, " +
                "t.transaction_type, " +
                "t.transaction_status, " +
                "t.payment_method, " +
                "t.location, " +
                "t.transaction_time, " +

                "r.risk_score, " +
                "r.risk_level, " +
                "r.risk_reason, " +
                "r.detection_method, " +

                "tr.review_status, " +
                "tr.user_description, " +
                "tr.admin_comment, " +
                "tr.reviewed_at " +

                "FROM transactions t " +

                "LEFT JOIN fraud_risk_scores r " +
                "ON t.transaction_id = r.transaction_id " +

                "LEFT JOIN transaction_reviews tr " +
                "ON t.transaction_id = tr.transaction_id " +

                "ORDER BY t.transaction_id DESC";


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {


            while (resultSet.next()) {

                DashboardTransaction transaction =
                        new DashboardTransaction();


                transaction.setTransactionId(
                        resultSet.getLong("transaction_id"));

                transaction.setSenderId(
                        resultSet.getInt("sender_id"));

                transaction.setReceiverId(
                        resultSet.getInt("receiver_id"));

                transaction.setAmount(
                        resultSet.getBigDecimal("amount"));

                transaction.setTransactionType(
                        resultSet.getString("transaction_type"));

                transaction.setTransactionStatus(
                        resultSet.getString("transaction_status"));

                transaction.setPaymentMethod(
                        resultSet.getString("payment_method"));

                transaction.setLocation(
                        resultSet.getString("location"));

                transaction.setTransactionTime(
                        resultSet.getTimestamp("transaction_time"));


                transaction.setRiskScore(
                        resultSet.getBigDecimal("risk_score"));

                transaction.setRiskLevel(
                        resultSet.getString("risk_level"));

                transaction.setRiskReason(
                        resultSet.getString("risk_reason"));

                transaction.setDetectionMethod(
                        resultSet.getString("detection_method"));


                transaction.setReviewStatus(
                        resultSet.getString("review_status"));

                transaction.setUserDescription(
                        resultSet.getString("user_description"));

                transaction.setAdminComment(
                        resultSet.getString("admin_comment"));

                transaction.setReviewedAt(
                        resultSet.getTimestamp("reviewed_at"));


                transactions.add(transaction);
            }


        } catch (Exception e) {

            System.out.println(
                    "Error loading dashboard transactions.");

            e.printStackTrace();
        }


        return transactions;
    }
 // =====================================================
 // GET USER TRANSACTION STATUS
 // =====================================================

 public DashboardTransaction getUserTransaction(
         int userId,
         long transactionId) {

     DashboardTransaction transaction = null;


     String sql =
             "SELECT " +
             "t.transaction_id, " +
             "t.sender_id, " +
             "t.receiver_id, " +
             "t.amount, " +
             "t.transaction_type, " +
             "t.transaction_status, " +
             "t.payment_method, " +
             "t.location, " +
             "t.transaction_time, " +

             "r.risk_score, " +
             "r.risk_level, " +
             "r.risk_reason, " +
             "r.detection_method, " +

             "tr.review_status, " +
             "tr.user_description, " +
             "tr.admin_comment, " +
             "tr.reviewed_at " +

             "FROM transactions t " +

             "LEFT JOIN fraud_risk_scores r " +
             "ON t.transaction_id = r.transaction_id " +

             "LEFT JOIN transaction_reviews tr " +
             "ON t.transaction_id = tr.transaction_id " +

             "WHERE t.transaction_id = ? " +
             "AND t.sender_id = ?";


     try (
             Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)
     ) {

         statement.setLong(1, transactionId);
         statement.setInt(2, userId);


         try (ResultSet resultSet =
                 statement.executeQuery()) {


             if (resultSet.next()) {

                 transaction =
                         new DashboardTransaction();


                 transaction.setTransactionId(
                         resultSet.getLong("transaction_id"));

                 transaction.setSenderId(
                         resultSet.getInt("sender_id"));

                 transaction.setReceiverId(
                         resultSet.getInt("receiver_id"));

                 transaction.setAmount(
                         resultSet.getBigDecimal("amount"));

                 transaction.setTransactionType(
                         resultSet.getString("transaction_type"));

                 transaction.setTransactionStatus(
                         resultSet.getString("transaction_status"));

                 transaction.setPaymentMethod(
                         resultSet.getString("payment_method"));

                 transaction.setLocation(
                         resultSet.getString("location"));

                 transaction.setTransactionTime(
                         resultSet.getTimestamp(
                                 "transaction_time"));


                 transaction.setRiskScore(
                         resultSet.getBigDecimal("risk_score"));

                 transaction.setRiskLevel(
                         resultSet.getString("risk_level"));

                 transaction.setRiskReason(
                         resultSet.getString("risk_reason"));

                 transaction.setDetectionMethod(
                         resultSet.getString(
                                 "detection_method"));


                 transaction.setReviewStatus(
                         resultSet.getString(
                                 "review_status"));

                 transaction.setUserDescription(
                         resultSet.getString(
                                 "user_description"));

                 transaction.setAdminComment(
                         resultSet.getString(
                                 "admin_comment"));

                 transaction.setReviewedAt(
                         resultSet.getTimestamp(
                                 "reviewed_at"));
             }
         }


     } catch (Exception e) {

         System.out.println(
                 "Error retrieving transaction status.");

         e.printStackTrace();
     }


     return transaction;
 }
}