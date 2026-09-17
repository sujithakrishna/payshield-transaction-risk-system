package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {

    private long transactionId;
    private int senderId;
    private Integer receiverId;
    private BigDecimal amount;
    private String transactionType;
    private String transactionStatus;
    private String paymentMethod;
    private String location;
    private Timestamp transactionTime;

    // Default constructor
    public Payment() {
    }

    // Parameterized constructor
    public Payment(long transactionId, int senderId, Integer receiverId,
                   BigDecimal amount, String transactionType,
                   String transactionStatus, String paymentMethod,
                   String location, Timestamp transactionTime) {

        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.paymentMethod = paymentMethod;
        this.location = location;
        this.transactionTime = transactionTime;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "transactionId=" + transactionId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", location='" + location + '\'' +
                ", transactionTime=" + transactionTime +
                '}';
    }
}