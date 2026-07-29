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

    // Get All Master Records
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMasters() {
        return ResponseEntity.ok(masterService.getAllMasters());
    }

    // Get Master Record By Id
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMasterById(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterById(id));
    }

    // Get Master Record By MRN Number
    @GetMapping("/get/mrn/{mrnNo}")
    public ResponseEntity<?> getByMrn(@PathVariable String mrnNo) {
        return ResponseEntity.ok(masterService.getByMrn(mrnNo));
    }

    // Delete Master Record
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.deleteMaster(id));
    }
}