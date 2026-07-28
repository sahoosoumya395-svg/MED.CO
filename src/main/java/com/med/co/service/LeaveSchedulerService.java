package com.med.co.service;

public interface LeaveSchedulerService {

    /**
     * Automatically update expired approved doctor leaves status from APPROVED to COMPLETED
     */
    void updateExpiredLeavesStatus();

}
