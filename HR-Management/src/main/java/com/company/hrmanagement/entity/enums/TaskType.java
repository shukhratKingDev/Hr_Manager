package com.company.hrmanagement.entity.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum TaskType {
    NEW("Task is new. Has not started yet.",Colour.BLUE),
    PROCESS("This task is in process and can be completed soon",Colour.YELLOW),
    DONE("This task has already done",Colour.GREEN);
    private String description;
    private Colour colour;


}

