package com.med.co.serviceimpl;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.med.co.entity.DoctorLeave;
import com.med.co.enums.LeaveStatus;
import com.med.co.repository.DoctorLeaveRepository;
import com.med.co.service.LeaveSchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveSchedulerServiceImpl implements LeaveSchedulerService {

    private final DoctorLeaveRepository doctorLeaveRepository;

    /**
     * Scheduled task that runs daily at 6:00 AM to update expired approved leaves status to COMPLETED
     * Cron: 0 0 6 * * * (runs at 6:00 AM every morning)
     */
    @Scheduled(cron = "0 0 6 * * *")
    @Override
    public void updateExpiredLeavesStatus() {
        try {
            log.info("Starting scheduled task to update expired doctor leaves status to COMPLETED...");
            
            // Find all approved leaves that have ended
            List<DoctorLeave> expiredLeaves = doctorLeaveRepository.findExpiredLeaves(LeaveStatus.APPROVED);
            
            if (!expiredLeaves.isEmpty()) {
                log.info("Found {} expired leaves to update", expiredLeaves.size());
                
                // Update all expired leaves status from APPROVED to COMPLETED
                for (DoctorLeave leave : expiredLeaves) {
                    leave.setStatus(LeaveStatus.COMPLETED);
                }
                doctorLeaveRepository.saveAll(expiredLeaves);
                
                log.info("Successfully updated {} expired leaves to COMPLETED status", expiredLeaves.size());
            } else {
                log.info("No expired leaves found to update");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating expired leaves status", e);
        }
    }

}
