package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.FraudAlert;
import util.DBConnection;

public class FraudAlertDAO {

    // =========================================================
    // CREATE FRAUD ALERT
    // =========================================================

    public boolean createAlert(FraudAlert alert) {

        String sql =
                "INSERT INTO fraud_alerts "
                + "(transaction_id, risk_score, alert_reason, alert_status) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             sql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(
                    1,
                    alert.getTransactionId()
            );

            preparedStatement.setBigDecimal(
                    2,
                    alert.getRiskScore()
            );

            preparedStatement.setString(
                    3,
                    alert.getAlertReason()
            );

            preparedStatement.setString(
                    4,
                    alert.getAlertStatus()
            );

            int rowsAffected =
                    preparedStatement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet resultSet =
                             preparedStatement.getGeneratedKeys()) {

                    if (resultSet.next()) {

                        alert.setAlertId(
                                resultSet.getLong(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // GET ALL FRAUD ALERTS
    // =========================================================

    public List<FraudAlert> getAllAlerts() {

        List<FraudAlert> alerts =
                new ArrayList<>();

        String sql =
                "SELECT alert_id, transaction_id, "
                + "risk_score, alert_reason, alert_status, created_at "
                + "FROM fraud_alerts "
                + "ORDER BY alert_id ASC";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                FraudAlert alert =
                        new FraudAlert();

                alert.setAlertId(
                        resultSet.getLong("alert_id")
                );

                alert.setTransactionId(
                        resultSet.getLong("transaction_id")
                );

                alert.setRiskScore(
                        resultSet.getBigDecimal("risk_score")
                );

                alert.setAlertReason(
                        resultSet.getString("alert_reason")
                );

                alert.setAlertStatus(
                        resultSet.getString("alert_status")
                );

                alert.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                alerts.add(alert);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return alerts;
    }


    // =========================================================
    // GET LATEST ALERT
    // =========================================================

    public FraudAlert getLatestAlert() {

        String sql =
                "SELECT alert_id, transaction_id, "
                + "risk_score, alert_reason, alert_status, created_at "
                + "FROM fraud_alerts "
                + "ORDER BY alert_id DESC "
                + "LIMIT 1";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     preparedStatement.executeQuery()) {

            if (resultSet.next()) {

                FraudAlert alert =
                        new FraudAlert();

                alert.setAlertId(
                        resultSet.getLong("alert_id")
                );

                alert.setTransactionId(
                        resultSet.getLong("transaction_id")
                );

                alert.setRiskScore(
                        resultSet.getBigDecimal("risk_score")
                );

                alert.setAlertReason(
                        resultSet.getString("alert_reason")
                );

                alert.setAlertStatus(
                        resultSet.getString("alert_status")
                );

                alert.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                return alert;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
}