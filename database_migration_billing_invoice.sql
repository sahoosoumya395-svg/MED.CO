-- Billing Invoice Table Migration
-- This migration script creates the billing_invoice table for the Billing Invoice module

CREATE TABLE IF NOT EXISTS billing_invoice (
    invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    invoice_date DATE NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_patient_name (patient_name),
    INDEX idx_invoice_date (invoice_date),
    INDEX idx_invoice_number (invoice_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
