package com.company.hrmanagement.dto;

import com.company.hrmanagement.entity.Company;
import com.company.hrmanagement.entity.Salary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull
    @Size(min = 3,max = 30)
    private String firstName;
    @NotNull
    @Size(min = 3,max = 50)
    private String lastName;
    @NotNull
    @Email
    @Size(min = 3,max = 50)
    private String email;
    @NotNull
    @Size(min = 7,max = 30)
    private String password;
    @NotNull
    private Company company;
    private Salary salary;//for employees
}
