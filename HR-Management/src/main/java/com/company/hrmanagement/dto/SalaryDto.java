package com.company.hrmanagement.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class SalaryDto {
    @NotNull
    private double amount;
    @NotNull
    @Email
    private String email;
}
