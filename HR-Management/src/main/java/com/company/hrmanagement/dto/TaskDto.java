package com.company.hrmanagement.dto;

import com.company.hrmanagement.entity.enums.TaskType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class TaskDto {
    @NotNull
    private String name;
    @NotNull
    private String caption;
    @NotNull
    private long deadline;
    @NotNull
    private TaskType taskCurrentType;
    @NotNull
    private Boolean done;
    @NotNull
    private String employeeUsername;
}
