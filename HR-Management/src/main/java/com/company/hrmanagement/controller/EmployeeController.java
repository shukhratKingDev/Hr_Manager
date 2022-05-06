package com.company.hrmanagement.controller;

import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.dto.SalaryDto;
import com.company.hrmanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/getAllEmployees")
    public ResponseEntity<?> getAllEmployees(){
        if (employeeService.getAllEmployees()!=null) {
           return ResponseEntity.ok(employeeService.getAllEmployees()) ;
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("You are not allowed to see the list of employees");
    }

    @PostMapping("/addSalary")
    public ResponseEntity<Response>addSalary(@RequestBody SalaryDto salaryDto){
        Response response=employeeService.addSalaryForEmployees(salaryDto);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/api/inside")
    public ResponseEntity<Response> inside(@RequestBody String username){
        Response response=employeeService.inside(username);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/outside")
    public ResponseEntity<Response> outside(@RequestBody String username){
        Response response=employeeService.outside(username);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }

}
