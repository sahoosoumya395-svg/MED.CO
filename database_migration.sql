-- SQL Migration Script for Doctor Availability API
-- This script creates the doctor_recurring_availability table and adds necessary methods to existing tables

-- ============================================================================================
-- 1. Create doctor_recurring_availability table
-- ============================================================================================
CREATE TABLE doctor_recurring_availability (
    recurring_availability_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    recurrence_type VARCHAR(50) NOT NULL DEFAULT 'MONTHLY_BY_DAY',
    day_of_month INT,
    day_of_week INT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    start_date DATE,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_day_of_month (day_of_month),
    INDEX idx_day_of_week (day_of_week),
    INDEX idx_active (active),
    INDEX idx_recurrence_type (recurrence_type)
);

-- ============================================================================================
-- 2. Sample Data: Doctor X available on days 1, 6, 8, 19, 29 every month at 09:00-12:00
-- ============================================================================================
-- Assuming Doctor with ID 1 exists (adjust doctor_id as needed)
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_month, start_time, end_time, active)
VALUES 
    (1, 'MONTHLY_BY_DAY', 1, '09:00:00', '12:00:00', TRUE),
    (1, 'MONTHLY_BY_DAY', 6, '09:00:00', '12:00:00', TRUE),
    (1, 'MONTHLY_BY_DAY', 8, '09:00:00', '12:00:00', TRUE),
    (1, 'MONTHLY_BY_DAY', 19, '09:00:00', '12:00:00', TRUE),
    (1, 'MONTHLY_BY_DAY', 29, '09:00:00', '12:00:00', TRUE);

-- ============================================================================================
-- 3. Additional Examples: Various recurrence patterns
-- ============================================================================================

-- Doctor 2: Available every Monday at 10:00-12:00 indefinitely
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_week, start_time, end_time, active)
VALUES (2, 'WEEKLY_BY_DAY', 1, '10:00:00', '12:00:00', TRUE);

-- Doctor 2: Available every Friday at 14:00-17:00 indefinitely
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_week, start_time, end_time, active)
VALUES (2, 'WEEKLY_BY_DAY', 5, '14:00:00', '17:00:00', TRUE);

-- Doctor 3: Available every day from 2026-07-01 to 2026-12-31 at 08:00-16:00
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, start_time, end_time, start_date, end_date, active)
VALUES (3, 'DAILY', '08:00:00', '16:00:00', '2026-07-01', '2026-12-31', TRUE);

-- Doctor 4: Available on 15th and 30th of each month at 09:00-13:00
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_month, start_time, end_time, active)
VALUES 
    (4, 'MONTHLY_BY_DAY', 15, '09:00:00', '13:00:00', TRUE),
    (4, 'MONTHLY_BY_DAY', 30, '09:00:00', '13:00:00', TRUE);

-- ============================================================================================
-- 4. Verify the tables and data
-- ============================================================================================
-- Check recurring availabilities created
SELECT * FROM doctor_recurring_availability;

-- Check doctors on approved leave (for exclusion logic)
SELECT DISTINCT d.id, d.firstName, d.lastName, dl.from_date, dl.to_date
FROM doctor d
LEFT JOIN doctor_leave dl ON d.id = dl.doctor_id AND dl.status = 'APPROVED'
WHERE CURRENT_DATE BETWEEN dl.from_date AND dl.to_date;

-- Check specific date availabilities
SELECT d.firstName, d.lastName, da.available_date, da.start_time, da.end_time
FROM doctor d
JOIN doctor_availability da ON d.id = da.doctor_id
WHERE da.available_date = CURRENT_DATE AND da.available = TRUE;

-- ============================================================================================
-- 5. Test Query: Count available doctors on 2026-07-26
-- ============================================================================================
-- This query simulates what the API does internally
WITH scheduled_doctors AS (
    -- Specific date availabilities
    SELECT DISTINCT da.doctor_id FROM doctor_availability da 
    WHERE da.available_date = '2026-07-26' AND da.available = TRUE
    
    UNION ALL
    
    -- Monthly recurring (26th of month)
    SELECT DISTINCT dra.doctor_id FROM doctor_recurring_availability dra
    WHERE dra.recurrence_type = 'MONTHLY_BY_DAY' AND dra.day_of_month = 26
    AND (dra.start_date IS NULL OR dra.start_date <= '2026-07-26')
    AND (dra.end_date IS NULL OR dra.end_date >= '2026-07-26')
    AND dra.active = TRUE
    
    UNION ALL
    
    -- Weekly recurring (Saturday = 6)
    SELECT DISTINCT dra.doctor_id FROM doctor_recurring_availability dra
    WHERE dra.recurrence_type = 'WEEKLY_BY_DAY' AND dra.day_of_week = 6
    AND (dra.start_date IS NULL OR dra.start_date <= '2026-07-26')
    AND (dra.end_date IS NULL OR dra.end_date >= '2026-07-26')
    AND dra.active = TRUE
    
    UNION ALL
    
    -- Daily recurring
    SELECT DISTINCT dra.doctor_id FROM doctor_recurring_availability dra
    WHERE dra.recurrence_type = 'DAILY'
    AND (dra.start_date IS NULL OR dra.start_date <= '2026-07-26')
    AND (dra.end_date IS NULL OR dra.end_date >= '2026-07-26')
    AND dra.active = TRUE
),
on_leave_doctors AS (
    SELECT DISTINCT doctor_id FROM doctor_leave
    WHERE status = 'APPROVED' AND from_date <= '2026-07-26' AND to_date >= '2026-07-26'
)
SELECT 
    COUNT(DISTINCT sd.doctor_id) as available_count,
    (SELECT COUNT(DISTINCT doctor_id) FROM scheduled_doctors) as total_scheduled,
    (SELECT COUNT(DISTINCT doctor_id) FROM on_leave_doctors) as total_on_leave
FROM scheduled_doctors sd
WHERE sd.doctor_id NOT IN (SELECT doctor_id FROM on_leave_doctors);

-- ============================================================================================
-- 6. Indexes for Performance
-- ============================================================================================
-- These indexes will improve query performance
CREATE INDEX idx_doctor_availability_date_available 
ON doctor_availability(available_date, available);

CREATE INDEX idx_doctor_leave_dates_status 
ON doctor_leave(from_date, to_date, status);

CREATE INDEX idx_doctor_recurring_date_range 
ON doctor_recurring_availability(start_date, end_date, active);

-- ============================================================================================
-- 7. Update existing doctor_availability table (if needed)
-- ============================================================================================
-- Ensure the available field has the right default
ALTER TABLE doctor_availability MODIFY available BOOLEAN NOT NULL DEFAULT TRUE;

-- ============================================================================================
-- 8. Reference: Recurrence Type Values
-- ============================================================================================
-- MONTHLY_BY_DAY: Doctor is available on specific day(s) of each month
--                 Use day_of_month (1-31)
-- 
-- WEEKLY_BY_DAY:  Doctor is available on specific day(s) of each week
--                 Use day_of_week (1=Monday, 2=Tuesday, ..., 7=Sunday)
--
-- DAILY:          Doctor is available every day (optionally within start_date/end_date range)
--                 Ignore day_of_month and day_of_week
-- ============================================================================================
