package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionReview {

    private long reviewId;
    private long transactionId;
    private int userId;

    private BigDecimal riskScore;
    private String riskLevel;
    private String riskReason;

    private String userDescription;

    private String reviewStatus;

    private String adminComment;

    private Timestamp createdAt;
    private Timestamp reviewedAt;


    // Default constructor
    public TransactionReview() {
    }


    // Getters and Setters

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }


    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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


    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }


    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public Timestamp getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }


    // Display review information
    @Override
    public String toString() {

        return "TransactionReview{" +
                "reviewId=" + reviewId +
                ", transactionId=" + transactionId +
                ", userId=" + userId +
                ", riskScore=" + riskScore +
                ", riskLevel='" + riskLevel + '\'' +
                ", riskReason='" + riskReason + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", reviewStatus='" + reviewStatus + '\'' +
                ", adminComment='" + adminComment + '\'' +
                ", createdAt=" + createdAt +
                ", reviewedAt=" + reviewedAt +
                '}';
    }
}