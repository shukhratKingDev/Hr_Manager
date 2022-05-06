package com.company.hrmanagement.controller;

import com.company.hrmanagement.dto.ReportDto;
import com.company.hrmanagement.service.ReportService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/getAllEmployee")
    public ResponseEntity<?> getReportAboutEmployees(){
        if (reportService.getReportAboutEmployees()!=null) {
           return ResponseEntity.ok(reportService.getReportAboutEmployees());
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("You can not get this information. This information is for only Hr_managers and Directors");
    }
}
