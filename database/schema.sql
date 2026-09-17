-- ============================================================
-- PayShield - Database Schema
-- Intelligent Transaction Risk & Payment Management System
-- ============================================================

CREATE DATABASE IF NOT EXISTS payment_fraud_detection;

USE payment_fraud_detection;


-- ============================================================
-- TRANSACTIONS
-- Stores all payment transactions
-- ============================================================

CREATE TABLE IF NOT EXISTS transactions (

    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    sender_id INT NOT NULL,

    receiver_id INT,

    amount DECIMAL(15,2) NOT NULL,

    transaction_type VARCHAR(50) NOT NULL,

    transaction_status VARCHAR(30) NOT NULL,

    payment_method VARCHAR(50) NOT NULL,

    location VARCHAR(100),

    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_sender_time (sender_id, transaction_time),

    INDEX idx_transaction_status (transaction_status)

);


-- ============================================================
-- FRAUD RISK SCORES
-- Stores the result of transaction risk analysis
-- ============================================================

CREATE TABLE IF NOT EXISTS fraud_risk_scores (

    risk_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    transaction_id BIGINT NOT NULL,

    risk_score DECIMAL(5,2) NOT NULL,

    risk_level VARCHAR(20) NOT NULL,

    risk_reason TEXT,

    detection_method VARCHAR(50) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_risk_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions(transaction_id)
        ON DELETE CASCADE,

    INDEX idx_risk_transaction (transaction_id),

    INDEX idx_risk_level (risk_level)

);


-- ============================================================
-- FRAUD ALERTS
-- Stores alerts generated for suspicious transactions
-- ============================================================

CREATE TABLE IF NOT EXISTS fraud_alerts (

    alert_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    transaction_id BIGINT NOT NULL,

    risk_score DECIMAL(5,2) NOT NULL,

    alert_reason TEXT,

    alert_status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_alert_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions(transaction_id)
        ON DELETE CASCADE,

    INDEX idx_alert_transaction (transaction_id),

    INDEX idx_alert_status (alert_status)

);


-- ============================================================
-- TRANSACTION REVIEWS
-- Stores user explanations and admin review decisions
-- ============================================================

CREATE TABLE IF NOT EXISTS transaction_reviews (

    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    transaction_id BIGINT NOT NULL,

    user_id INT NOT NULL,

    risk_score DECIMAL(5,2) NOT NULL,

    risk_level VARCHAR(20) NOT NULL,

    risk_reason TEXT,

    user_description TEXT,

    review_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    admin_comment TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    reviewed_at TIMESTAMP NULL,

    CONSTRAINT fk_review_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions(transaction_id)
        ON DELETE CASCADE,

    INDEX idx_review_transaction (transaction_id),

    INDEX idx_review_user (user_id),

    INDEX idx_review_status (review_status)

);