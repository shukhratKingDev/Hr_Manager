package com.company.hrmanagement.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateDto {
    @NotNull
    @Email
    @Size(min = 5)
    private String email;
    @NotNull
    @Size(min=3)
    private String password;

}
