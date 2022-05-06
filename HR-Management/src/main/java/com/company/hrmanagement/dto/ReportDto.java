package com.company.hrmanagement.dto;

import com.company.hrmanagement.entity.Salary;
import com.company.hrmanagement.entity.Task;
import com.company.hrmanagement.entity.Turnstile;
import com.company.hrmanagement.entity.User;
import lombok.Data;

@Data
public class ReportDto {
    private User employee;
    private Task task;
    private Salary salary;
    private Turnstile turnstile;
}
