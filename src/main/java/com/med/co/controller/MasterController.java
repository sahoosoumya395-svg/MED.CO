package com.med.co.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.service.MasterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MasterController {

    private final MasterService masterService;

    // Get all master records
    @GetMapping("/all")
    public ResponseEntity<?> getAllMasters() {
        return ResponseEntity.ok(masterService.getAllMasters());
    }

    // Get master record by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMasterById(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterById(id));
    }
    
    
    @GetMapping("/mrn/{mrnNo}")
    public ResponseEntity<?> getByMrn(@PathVariable String mrnNo) {
        return ResponseEntity.ok(masterService.getByMrn(mrnNo));
    }
    

    // Delete master record (Optional)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.deleteMaster(id));
    }
}