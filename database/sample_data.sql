-- ============================================================
-- PayShield - Sample Data
-- Intelligent Transaction Risk & Payment Management System
-- ============================================================

USE payment_fraud_detection;


-- ============================================================
-- SAMPLE TRANSACTIONS
-- ============================================================

INSERT INTO transactions
(transaction_id, sender_id, receiver_id, amount,
 transaction_type, transaction_status, payment_method,
 location, transaction_time)
VALUES

-- Low-risk transaction
(1001, 101, 201, 2500.00,
 'TRANSFER', 'SUCCESS', 'UPI',
 'Chennai',
 '2026-09-17 09:00:00'),

-- Medium-risk transaction
(1002, 102, 202, 15000.00,
 'TRANSFER', 'PENDING_REVIEW', 'UPI',
 'Chennai',
 '2026-09-17 10:00:00'),

-- High-risk transaction
(1003, 103, 203, 25000.00,
 'TRANSFER', 'PENDING_REVIEW', 'CARD',
 'Bangalore',
 '2026-09-17 10:05:00'),

-- Approved transaction after admin review
(1004, 104, 204, 18000.00,
 'TRANSFER', 'SUCCESS', 'BANK_TRANSFER',
 'Chennai',
 '2026-09-17 11:00:00'),

-- Rejected transaction after admin review
(1005, 105, 205, 22000.00,
 'TRANSFER', 'REJECTED', 'UPI',
 'Mumbai',
 '2026-09-17 11:10:00');


-- ============================================================
-- SAMPLE FRAUD RISK SCORES
-- ============================================================

INSERT INTO fraud_risk_scores
(transaction_id, risk_score, risk_level,
 risk_reason, detection_method)
VALUES

(1001, 0.00, 'LOW',
 'No suspicious activity detected',
 'RULE_BASED'),

(1002, 40.00, 'MEDIUM',
 'High transaction amount',
 'RULE_BASED'),

(1003, 70.00, 'HIGH',
 'High transaction amount; Transaction from different location',
 'RULE_BASED'),

(1004, 40.00, 'MEDIUM',
 'High transaction amount',
 'RULE_BASED'),

(1005, 70.00, 'HIGH',
 'High transaction amount; Transaction from different location',
 'RULE_BASED');


-- ============================================================
-- SAMPLE FRAUD ALERTS
-- ============================================================

INSERT INTO fraud_alerts
(transaction_id, risk_score, alert_reason, alert_status)
VALUES

(1003, 70.00,
 'High transaction amount; Transaction from different location',
 'OPEN'),

(1005, 70.00,
 'High transaction amount; Transaction from different location',
 'OPEN');


-- ============================================================
-- SAMPLE TRANSACTION REVIEWS
-- ============================================================

INSERT INTO transaction_reviews
(transaction_id, user_id, risk_score, risk_level,
 risk_reason, user_description, review_status,
 admin_comment, reviewed_at)
VALUES

-- Pending review
(1002, 102, 40.00, 'MEDIUM',
 'High transaction amount',
 'This was a legitimate transfer for a personal purchase.',
 'PENDING',
 NULL,
 NULL),

-- Pending high-risk review
(1003, 103, 70.00, 'HIGH',
 'High transaction amount; Transaction from different location',
 'I made this payment while travelling.',
 'PENDING',
 NULL,
 NULL),

-- Approved review
(1004, 104, 40.00, 'MEDIUM',
 'High transaction amount',
 'This payment was made for a verified business transaction.',
 'APPROVED',
 'Transaction details verified and approved.',
 '2026-09-17 11:05:00'),

-- Rejected review
(1005, 105, 70.00, 'HIGH',
 'High transaction amount; Transaction from different location',
 'I do not recognize this transaction.',
 'REJECTED',
 'Transaction rejected after review due to suspicious activity.',
 '2026-09-17 11:20:00');