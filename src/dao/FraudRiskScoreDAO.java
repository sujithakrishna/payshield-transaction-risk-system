package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.FraudRiskScore;
import util.DBConnection;

public class FraudRiskScoreDAO {

    public boolean saveRiskScore(FraudRiskScore riskScore) {

        String sql = "INSERT INTO fraud_risk_scores "
                + "(transaction_id, risk_score, risk_level, "
                + "risk_reason, detection_method) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, riskScore.getTransactionId());
            statement.setBigDecimal(2, riskScore.getRiskScore());
            statement.setString(3, riskScore.getRiskLevel());
            statement.setString(4, riskScore.getRiskReason());
            statement.setString(5, riskScore.getDetectionMethod());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}