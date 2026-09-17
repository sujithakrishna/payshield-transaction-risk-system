package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FraudRiskScore {

    private long riskId;
    private long transactionId;
    private BigDecimal riskScore;
    private String riskLevel;
    private String riskReason;
    private String detectionMethod;
    private Timestamp createdAt;

    // Default constructor
    public FraudRiskScore() {
    }

    // Parameterized constructor
    public FraudRiskScore(long riskId,
                          long transactionId,
                          BigDecimal riskScore,
                          String riskLevel,
                          String riskReason,
                          String detectionMethod,
                          Timestamp createdAt) {

        this.riskId = riskId;
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.riskReason = riskReason;
        this.detectionMethod = detectionMethod;
        this.createdAt = createdAt;
    }

    public long getRiskId() {
        return riskId;
    }

    public void setRiskId(long riskId) {
        this.riskId = riskId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(BigDecimal riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskReason() {
        return riskReason;
    }

    public void setRiskReason(String riskReason) {
        this.riskReason = riskReason;
    }

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FraudRiskScore{" +
                "riskId=" + riskId +
                ", transactionId=" + transactionId +
                ", riskScore=" + riskScore +
                ", riskLevel='" + riskLevel + '\'' +
                ", riskReason='" + riskReason + '\'' +
                ", detectionMethod='" + detectionMethod + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}