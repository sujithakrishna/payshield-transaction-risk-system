package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Model.TransactionReview;
import util.DBConnection;

public class TransactionReviewDAO {

    // Create a new review
    public void createReview(TransactionReview review) {

        String sql = "INSERT INTO transaction_reviews "
                   + "(transaction_id, user_id, risk_score, risk_level, risk_reason, "
                   + "user_description, review_status, admin_comment, reviewed_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, review.getTransactionId());
            statement.setInt(2, review.getUserId());
            statement.setBigDecimal(3, review.getRiskScore());
            statement.setString(4, review.getRiskLevel());
            statement.setString(5, review.getRiskReason());
            statement.setString(6, review.getUserDescription());
            statement.setString(7, review.getReviewStatus());
            statement.setString(8, review.getAdminComment());
            statement.setTimestamp(9, review.getReviewedAt());

            statement.executeUpdate();

            System.out.println("Transaction review created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get all pending reviews
    public List<TransactionReview> getPendingReviews() {

        List<TransactionReview> reviews = new ArrayList<>();

        String sql = "SELECT * FROM transaction_reviews "
                   + "WHERE review_status = 'PENDING' "
                   + "ORDER BY created_at ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                TransactionReview review = new TransactionReview();

                review.setReviewId(resultSet.getLong("review_id"));
                review.setTransactionId(resultSet.getLong("transaction_id"));
                review.setUserId(resultSet.getInt("user_id"));
                review.setRiskScore(resultSet.getBigDecimal("risk_score"));
                review.setRiskLevel(resultSet.getString("risk_level"));
                review.setRiskReason(resultSet.getString("risk_reason"));
                review.setUserDescription(resultSet.getString("user_description"));
                review.setReviewStatus(resultSet.getString("review_status"));
                review.setAdminComment(resultSet.getString("admin_comment"));
                review.setCreatedAt(resultSet.getTimestamp("created_at"));
                review.setReviewedAt(resultSet.getTimestamp("reviewed_at"));

                reviews.add(review);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // Get review using transaction ID
    public TransactionReview getReviewByTransactionId(long transactionId) {

        String sql = "SELECT * FROM transaction_reviews "
                   + "WHERE transaction_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, transactionId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    TransactionReview review = new TransactionReview();

                    review.setReviewId(resultSet.getLong("review_id"));
                    review.setTransactionId(resultSet.getLong("transaction_id"));
                    review.setUserId(resultSet.getInt("user_id"));
                    review.setRiskScore(resultSet.getBigDecimal("risk_score"));
                    review.setRiskLevel(resultSet.getString("risk_level"));
                    review.setRiskReason(resultSet.getString("risk_reason"));
                    review.setUserDescription(resultSet.getString("user_description"));
                    review.setReviewStatus(resultSet.getString("review_status"));
                    review.setAdminComment(resultSet.getString("admin_comment"));
                    review.setCreatedAt(resultSet.getTimestamp("created_at"));
                    review.setReviewedAt(resultSet.getTimestamp("reviewed_at"));

                    return review;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add or update user's explanation
    public void addUserDescription(long reviewId, String description) {

        String sql = "UPDATE transaction_reviews "
                   + "SET user_description = ? "
                   + "WHERE review_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, description);
            statement.setLong(2, reviewId);

            statement.executeUpdate();

            System.out.println("User description added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Approve the transaction
    public void approveReview(long reviewId, String adminComment) {

        String sql = "UPDATE transaction_reviews "
                   + "SET review_status = 'APPROVED', "
                   + "admin_comment = ?, "
                   + "reviewed_at = CURRENT_TIMESTAMP "
                   + "WHERE review_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, adminComment);
            statement.setLong(2, reviewId);

            statement.executeUpdate();

            System.out.println("Review approved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reject the transaction
    public void rejectReview(long reviewId, String adminComment) {

        String sql = "UPDATE transaction_reviews "
                   + "SET review_status = 'REJECTED', "
                   + "admin_comment = ?, "
                   + "reviewed_at = CURRENT_TIMESTAMP "
                   + "WHERE review_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, adminComment);
            statement.setLong(2, reviewId);

            statement.executeUpdate();

            System.out.println("Review rejected successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update transaction status after admin decision
    public void updateTransactionStatus(long transactionId, String status) {

        String sql = "UPDATE transactions "
                   + "SET transaction_status = ? "
                   + "WHERE transaction_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, transactionId);

            statement.executeUpdate();

            System.out.println("Transaction status updated to: " + status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // Approve review and complete the transaction
    public void approveTransaction(long reviewId, long transactionId, String adminComment) {

        String reviewSql = "UPDATE transaction_reviews "
                         + "SET review_status = 'APPROVED', "
                         + "admin_comment = ?, "
                         + "reviewed_at = CURRENT_TIMESTAMP "
                         + "WHERE review_id = ? "
                         + "AND review_status = 'PENDING'";

        String transactionSql = "UPDATE transactions "
                              + "SET transaction_status = 'SUCCESS' "
                              + "WHERE transaction_id = ? "
                              + "AND transaction_status = 'PENDING_REVIEW'";

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            // Update review
            try (PreparedStatement statement =
                         connection.prepareStatement(reviewSql)) {

                statement.setString(1, adminComment);
                statement.setLong(2, reviewId);

                int reviewUpdated = statement.executeUpdate();

                if (reviewUpdated == 0) {
                    throw new Exception(
                            "Review was not found or is already processed.");
                }
            }


            // Update transaction
            try (PreparedStatement statement =
                         connection.prepareStatement(transactionSql)) {

                statement.setLong(1, transactionId);

                int transactionUpdated = statement.executeUpdate();

                if (transactionUpdated == 0) {
                    throw new Exception(
                            "Transaction was not in PENDING_REVIEW status.");
                }
            }


            connection.commit();

            System.out.println("Transaction approved successfully.");
            System.out.println("Transaction status changed to SUCCESS.");

        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            System.out.println("Transaction approval failed.");
            e.printStackTrace();

        } finally {

            try {

                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }

            } catch (Exception closeException) {
                closeException.printStackTrace();
            }
        }
    }


    // Reject review and reject the transaction
    public void rejectTransaction(long reviewId, long transactionId, String adminComment) {

        String reviewSql = "UPDATE transaction_reviews "
                         + "SET review_status = 'REJECTED', "
                         + "admin_comment = ?, "
                         + "reviewed_at = CURRENT_TIMESTAMP "
                         + "WHERE review_id = ? "
                         + "AND review_status = 'PENDING'";

        String transactionSql = "UPDATE transactions "
                              + "SET transaction_status = 'REJECTED' "
                              + "WHERE transaction_id = ? "
                              + "AND transaction_status = 'PENDING_REVIEW'";

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            // Update review
            try (PreparedStatement statement =
                         connection.prepareStatement(reviewSql)) {

                statement.setString(1, adminComment);
                statement.setLong(2, reviewId);

                int reviewUpdated = statement.executeUpdate();

                if (reviewUpdated == 0) {
                    throw new Exception(
                            "Review was not found or is already processed.");
                }
            }


            // Update transaction
            try (PreparedStatement statement =
                         connection.prepareStatement(transactionSql)) {

                statement.setLong(1, transactionId);

                int transactionUpdated = statement.executeUpdate();

                if (transactionUpdated == 0) {
                    throw new Exception(
                            "Transaction was not in PENDING_REVIEW status.");
                }
            }


            connection.commit();

            System.out.println("Transaction rejected successfully.");
            System.out.println("Transaction status changed to REJECTED.");

        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            System.out.println("Transaction rejection failed.");
            e.printStackTrace();

        } finally {

            try {

                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }

            } catch (Exception closeException) {
                closeException.printStackTrace();
            }
        }
    }
}