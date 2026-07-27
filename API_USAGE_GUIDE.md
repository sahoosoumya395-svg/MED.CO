# Doctor Availability Count API - Implementation Guide

## Overview
This implementation provides a complete API to check how many doctors are available on a specific date by:
1. Fetching all doctors scheduled (via recurring patterns or specific dates)
2. Subtracting doctors who are on approved leave on that date

## Database Schema

### 1. DoctorRecurringAvailability Table
Stores recurring availability patterns for doctors.

**Fields:**
- `recurring_availability_id` (Long, PK)
- `doctor_id` (Long, FK → doctor.id)
- `recurrence_type` (ENUM: MONTHLY_BY_DAY, WEEKLY_BY_DAY, DAILY)
- `day_of_month` (Integer) - For monthly recurrence (1-31)
- `day_of_week` (Integer) - For weekly recurrence (1=MON, 7=SUN)
- `start_time` (LocalTime) - e.g., 09:00
- `end_time` (LocalTime) - e.g., 12:00
- `start_date` (LocalDate) - Optional validity start
- `end_date` (LocalDate) - Optional validity end
- `active` (Boolean) - Default: true

**Example: Doctor X available on days 1, 6, 8, 19, 29 each month from 09:00-12:00**
```
recurring_availability_id: 1, doctor_id: 42, recurrence_type: MONTHLY_BY_DAY, day_of_month: 1, start_time: 09:00, end_time: 12:00, active: true
recurring_availability_id: 2, doctor_id: 42, recurrence_type: MONTHLY_BY_DAY, day_of_month: 6, start_time: 09:00, end_time: 12:00, active: true
recurring_availability_id: 3, doctor_id: 42, recurrence_type: MONTHLY_BY_DAY, day_of_month: 8, start_time: 09:00, end_time: 12:00, active: true
recurring_availability_id: 4, doctor_id: 42, recurrence_type: MONTHLY_BY_DAY, day_of_month: 19, start_time: 09:00, end_time: 12:00, active: true
recurring_availability_id: 5, doctor_id: 42, recurrence_type: MONTHLY_BY_DAY, day_of_month: 29, start_time: 09:00, end_time: 12:00, active: true
```

### 2. DoctorAvailability Table (Existing)
Stores specific date availability for doctors.

**Fields:**
- `availability_id` (Long, PK)
- `doctor_id` (Long, FK)
- `available_date` (LocalDate)
- `start_time` (LocalTime)
- `end_time` (LocalTime)
- `available` (Boolean)

### 3. DoctorLeave Table (Existing)
Stores leave information for doctors.

**Fields:**
- `leave_id` (Long, PK)
- `doctor_id` (Long, FK)
- `leave_type` (ENUM: LeaveType)
- `from_date` (LocalDate)
- `to_date` (LocalDate)
- `reason` (String)
- `status` (ENUM: PENDING, APPROVED, REJECTED, COMPLETED)

## API Endpoint

### Count Available Doctors on a Date

**Endpoint:** `GET /api/doctor-availability/available/count`

**Query Parameter:**
- `date` (required, format: yyyy-MM-dd) - The date to check availability

**Response:**
```json
{
  "date": "2026-07-26",
  "availableCount": 5,
  "totalScheduledRecurring": 12,
  "totalScheduledSpecific": 2,
  "totalOnLeaveApproved": 3,
  "message": "Available doctors count calculated successfully"
}
```

**Response Fields:**
- `date` - The queried date
- `availableCount` - Number of doctors available after subtracting leaves
- `totalScheduledRecurring` - Total doctors with recurring availability on this date
- `totalScheduledSpecific` - Total doctors with specific date availability
- `totalOnLeaveApproved` - Total doctors on approved leave on this date
- `message` - Status message

## Example cURL Requests

### Example 1: Check availability on 2026-07-26 (Today)
```bash
curl -X GET "http://localhost:8080/api/doctor-availability/available/count?date=2026-07-26"
```

### Example 2: Check availability on 2026-08-06 (First day of next month)
```bash
curl -X GET "http://localhost:8080/api/doctor-availability/available/count?date=2026-08-06"
```

### Example 3: PowerShell Request
```powershell
$uri = "http://localhost:8080/api/doctor-availability/available/count?date=2026-07-26"
$response = Invoke-RestMethod -Uri $uri -Method Get
$response | ConvertTo-Json
```

## How the Logic Works

### Step-by-Step Process:

1. **Fetch Specific Date Availabilities**
   - Query: `SELECT DISTINCT doctor_id FROM DoctorAvailability WHERE available_date = :date AND available = true`
   - Gets doctors scheduled for a specific date

2. **Fetch Recurring Availabilities**
   - **Monthly Pattern:**
     - Query: Find all doctors with day_of_month matching the query date's day of month
     - Example: For 2026-07-26, find all doctors available on day 26 every month
   
   - **Weekly Pattern:**
     - Query: Find all doctors with day_of_week matching the query date's day of week
     - Example: For 2026-07-26 (Saturday, day 6), find all Saturday-available doctors
   
   - **Daily Pattern:**
     - Query: Find all doctors available every day within their validity window

3. **Combine All Scheduled Doctors**
   - Merge results from specific date + all recurring patterns
   - Use Set to eliminate duplicates

4. **Fetch Doctors on Leave**
   - Query: Find all doctors with APPROVED status where: `from_date <= query_date <= to_date`

5. **Calculate Available Doctors**
   - Formula: `Available = AllScheduled - OnLeave`
   - Remove any doctor on leave from the scheduled set

## Data Entry Examples

### Adding Recurring Availability (SQL)
```sql
-- Doctor 1: Available on Mondays at 10:00-12:00 indefinitely
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_week, start_time, end_time, active)
VALUES (1, 'WEEKLY_BY_DAY', 1, '10:00:00', '12:00:00', true);

-- Doctor 2: Available on 1st, 15th of each month at 09:00-13:00
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_month, start_time, end_time, active)
VALUES (2, 'MONTHLY_BY_DAY', 1, '09:00:00', '13:00:00', true);

INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, day_of_month, start_time, end_time, active)
VALUES (2, 'MONTHLY_BY_DAY', 15, '09:00:00', '13:00:00', true);

-- Doctor 3: Available daily from 2026-07-01 to 2026-12-31
INSERT INTO doctor_recurring_availability (doctor_id, recurrence_type, start_time, end_time, start_date, end_date, active)
VALUES (3, 'DAILY', '08:00:00', '16:00:00', '2026-07-01', '2026-12-31', true);
```

### Adding Specific Date Availability (SQL)
```sql
-- Doctor 5 available on 2026-07-26 from 14:00-16:00
INSERT INTO doctor_availability (doctor_id, available_date, start_time, end_time, available)
VALUES (5, '2026-07-26', '14:00:00', '16:00:00', true);
```

### Adding Doctor Leave (SQL)
```sql
-- Doctor 4 on leave from 2026-07-20 to 2026-07-25
INSERT INTO doctor_leave (doctor_id, leave_type, from_date, to_date, reason, status, created_at, updated_at)
VALUES (4, 'SICK', '2026-07-20', '2026-07-25', 'Medical appointment', 'APPROVED', NOW(), NOW());
```

## Test Cases

### Test 1: Normal Day (Multiple doctors available)
**Setup:**
- Doctor 1: Recurring MONTHLY_BY_DAY day 26, active
- Doctor 2: Recurring WEEKLY_BY_DAY day 6 (Saturday), active
- Doctor 3: Specific date 2026-07-26, available=true
- Doctor 4: On leave 2026-07-26 (status=APPROVED)

**Query:** GET /api/doctor-availability/available/count?date=2026-07-26

**Expected Response:**
```json
{
  "date": "2026-07-26",
  "availableCount": 3,
  "totalScheduledRecurring": 2,
  "totalScheduledSpecific": 1,
  "totalOnLeaveApproved": 1,
  "message": "Available doctors count calculated successfully"
}
```

### Test 2: End of Month (Days 30, 31)
**Query:** GET /api/doctor-availability/available/count?date=2026-07-31

**Note:** Doctors with day_of_month=31 will show as available; day_of_month=30 will not match this date.

### Test 3: Leap Year (Feb 29)
**Query:** GET /api/doctor-availability/available/count?date=2024-02-29

**Behavior:** Doctors with day_of_month=29 will be available on this date.

### Test 4: No Doctors Available
**Setup:**
- No doctors with availability on the query date
- Or all scheduled doctors are on leave

**Expected Response:**
```json
{
  "date": "2026-07-26",
  "availableCount": 0,
  "totalScheduledRecurring": 0,
  "totalScheduledSpecific": 0,
  "totalOnLeaveApproved": 0,
  "message": "Available doctors count calculated successfully"
}
```

## Additional API Endpoints (Existing)

### Add Doctor Availability (Specific Date)
**POST** `/api/doctor-availability`
```json
{
  "doctorId": 1,
  "availableDate": "2026-07-26",
  "startTime": "09:00:00",
  "endTime": "12:00:00",
  "available": true
}
```

### Get All Availabilities for a Doctor
**GET** `/api/doctor-availability/{doctorId}`

### Update Availability
**PUT** `/api/doctor-availability/{availabilityId}`

### Delete Availability
**DELETE** `/api/doctor-availability/{availabilityId}`

## Files Modified/Created

### New Files:
1. `com/med/co/entity/DoctorRecurringAvailability.java` - Entity for recurring patterns
2. `com/med/co/repository/DoctorRecurringAvailabilityRepository.java` - Repository with queries
3. `com/med/co/dto/response/AvailableDoctorsCountResponse.java` - Response DTO

### Modified Files:
1. `com/med/co/service/DoctorAvailabilityService.java` - Added new method
2. `com/med/co/serviceimpl/DoctorAvailabilityServiceImpl.java` - Implemented count logic
3. `com/med/co/controller/DoctorAvailabilityController.java` - Added endpoint
4. `com/med/co/repository/DoctorAvailabilityRepository.java` - Added query methods
5. `com/med/co/repository/DoctorLeaveRepository.java` - Added query method

## Performance Considerations

1. **Database Indexes:** Add indexes on:
   - `doctor_recurring_availability(day_of_month, active)`
   - `doctor_recurring_availability(day_of_week, active)`
   - `doctor_availability(available_date, available)`
   - `doctor_leave(from_date, to_date, status)`

2. **Caching:** For repeated queries on the same date within short intervals, implement caching with short TTL (5-15 minutes)

3. **Query Optimization:** The queries use batch operations (UNION) which is efficient for small result sets

## Edge Cases Handled

1. ✅ Doctors on multiple recurrence types (weekly + monthly) are counted once
2. ✅ Doctors on leave are completely excluded from the available count
3. ✅ Specific date availability is merged with recurring patterns
4. ✅ Invalid dates (Feb 30) return 0 available doctors
5. ✅ End-of-month dates handle day_of_month boundaries correctly
6. ✅ Leap years and leap days are handled by Java's LocalDate

## Build and Deploy

```bash
# Clean build
mvn clean install

# Run tests
mvn test

# Build and run
java -jar target/MED.CO_CLONE-0.0.1-SNAPSHOT.jar
```

The application runs on the default Spring Boot port (usually 8080). Verify with:
```bash
curl http://localhost:8080/actuator/health
```

---

**Created:** 2026-07-26  
**Implementation:** Complete with recurring availability patterns and leave handling
