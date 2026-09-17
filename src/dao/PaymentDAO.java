package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Payment;
import util.DBConnection;

public class PaymentDAO {

    // =========================================================
    // ADD PAYMENT
    // =========================================================

    public boolean addPayment(Payment payment) {

        String sql =
                "INSERT INTO transactions "
                + "(sender_id, receiver_id, amount, transaction_type, "
                + "transaction_status, payment_method, location) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             sql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(
                    1, payment.getSenderId());

            preparedStatement.setInt(
                    2, payment.getReceiverId());

            preparedStatement.setBigDecimal(
                    3, payment.getAmount());

            preparedStatement.setString(
                    4, payment.getTransactionType());

            preparedStatement.setString(
                    5, payment.getTransactionStatus());

            preparedStatement.setString(
                    6, payment.getPaymentMethod());

            preparedStatement.setString(
                    7, payment.getLocation());

            int rowsAffected =
                    preparedStatement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet resultSet =
                             preparedStatement.getGeneratedKeys()) {

                    if (resultSet.next()) {

                        payment.setTransactionId(
                                resultSet.getLong(1));
                    }
                }

                System.out.println(
                        "Payment added successfully.");

                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    public List<Payment> getAllPayments() {

        List<Payment> payments =
                new ArrayList<>();

        String sql =
                "SELECT transaction_id, sender_id, receiver_id, "
                + "amount, transaction_type, transaction_status, "
                + "payment_method, location, transaction_time "
                + "FROM transactions "
                + "ORDER BY transaction_id ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment =
                        new Payment();

                payment.setTransactionId(
                        resultSet.getLong(
                                "transaction_id"));

                payment.setSenderId(
                        resultSet.getInt(
                                "sender_id"));

                payment.setReceiverId(
                        resultSet.getInt(
                                "receiver_id"));

                payment.setAmount(
                        resultSet.getBigDecimal(
                                "amount"));

                payment.setTransactionType(
                        resultSet.getString(
                                "transaction_type"));

                payment.setTransactionStatus(
                        resultSet.getString(
                                "transaction_status"));

                payment.setPaymentMethod(
                        resultSet.getString(
                                "payment_method"));

                payment.setLocation(
                        resultSet.getString(
                                "location"));

                payment.setTransactionTime(
                        resultSet.getTimestamp(
                                "transaction_time"));

                payments.add(payment);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return payments;
    }


    // =========================================================
    // RULE 2
    // HIGH TRANSACTION FREQUENCY
    // =========================================================

    public int countPreviousTransactions(
            int senderId,
            long transactionId) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM transactions p "
                + "JOIN transactions t "
                + "ON t.transaction_id = ? "
                + "WHERE p.sender_id = ? "
                + "AND p.transaction_id <> t.transaction_id "
                + "AND p.transaction_time >= "
                + "t.transaction_time - INTERVAL 10 MINUTE "
                + "AND ( "
                + "p.transaction_time < t.transaction_time "
                + "OR "
                + "(p.transaction_time = t.transaction_time "
                + "AND p.transaction_id < t.transaction_id) "
                + ")";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setLong(
                    1, transactionId);

            preparedStatement.setInt(
                    2, senderId);

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }


    // =========================================================
    // GET LATEST PAYMENT
    // =========================================================

    public Payment getLatestPayment() {

        String sql =
                "SELECT transaction_id, sender_id, receiver_id, "
                + "amount, transaction_type, transaction_status, "
                + "payment_method, location, transaction_time "
                + "FROM transactions "
                + "ORDER BY transaction_id DESC "
                + "LIMIT 1";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     preparedStatement.executeQuery()) {

            if (resultSet.next()) {

                Payment payment =
                        new Payment();

                payment.setTransactionId(
                        resultSet.getLong(
                                "transaction_id"));

                payment.setSenderId(
                        resultSet.getInt(
                                "sender_id"));

                payment.setReceiverId(
                        resultSet.getInt(
                                "receiver_id"));

                payment.setAmount(
                        resultSet.getBigDecimal(
                                "amount"));

                payment.setTransactionType(
                        resultSet.getString(
                                "transaction_type"));

                payment.setTransactionStatus(
                        resultSet.getString(
                                "transaction_status"));

                payment.setPaymentMethod(
                        resultSet.getString(
                                "payment_method"));

                payment.setLocation(
                        resultSet.getString(
                                "location"));

                payment.setTransactionTime(
                        resultSet.getTimestamp(
                                "transaction_time"));

                return payment;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // RULE 3
    // DUPLICATE TRANSACTION
    // =========================================================

    public int countDuplicateTransactions(
            Payment payment) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM transactions "
                + "WHERE sender_id = ? "
                + "AND receiver_id = ? "
                + "AND amount = ? "
                + "AND transaction_type = ? "
                + "AND transaction_id <> ? "
                + "AND transaction_time >= ( "
                + "SELECT transaction_time "
                + "FROM transactions "
                + "WHERE transaction_id = ? "
                + ") - INTERVAL 5 MINUTE "
                + "AND transaction_time < ( "
                + "SELECT transaction_time "
                + "FROM transactions "
                + "WHERE transaction_id = ? "
                + ")";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setInt(
                    1, payment.getSenderId());

            preparedStatement.setInt(
                    2, payment.getReceiverId());

            preparedStatement.setBigDecimal(
                    3, payment.getAmount());

            preparedStatement.setString(
                    4, payment.getTransactionType());

            preparedStatement.setLong(
                    5, payment.getTransactionId());

            preparedStatement.setLong(
                    6, payment.getTransactionId());

            preparedStatement.setLong(
                    7, payment.getTransactionId());

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }


    // =========================================================
    // RULE 4
    // DIFFERENT LOCATION WITHIN 10 MINUTES
    // =========================================================

    public int countDifferentLocationTransactions(
            Payment payment) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM transactions p "
                + "JOIN transactions t "
                + "ON t.transaction_id = ? "
                + "WHERE p.sender_id = ? "
                + "AND p.transaction_id <> t.transaction_id "
                + "AND p.location IS NOT NULL "
                + "AND t.location IS NOT NULL "
                + "AND p.location <> t.location "
                + "AND p.transaction_time >= "
                + "t.transaction_time - INTERVAL 10 MINUTE "
                + "AND ( "
                + "p.transaction_time < t.transaction_time "
                + "OR "
                + "(p.transaction_time = t.transaction_time "
                + "AND p.transaction_id < t.transaction_id) "
                + ")";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            // Transaction being analyzed
            preparedStatement.setLong(
                    1,
                    payment.getTransactionId());

            // Sender of the transaction
            preparedStatement.setInt(
                    2,
                    payment.getSenderId());

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }
}