package com.company.hrmanagement.controller;

import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.dto.TaskDto;
import com.company.hrmanagement.entity.Task;
import com.company.hrmanagement.service.AuthService;
import com.company.hrmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class TaskController {
    private final TaskService taskService;
    private final AuthService authService;
@Autowired
    public TaskController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }
    @PostMapping("/myTask")
    public ResponseEntity<Response> addTask(@RequestBody TaskDto taskDto){
    Response response=taskService.addTask(taskDto);
    return ResponseEntity.status(response.isSuccess()? HttpStatus.CREATED:HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/myTask/{id}")
    public ResponseEntity<Task> getMyTask(@PathVariable UUID id){
    return ResponseEntity.ok().body(taskService.getMyTask(id));
    }

    @PostMapping("/taskIsDone")
    public ResponseEntity<Response> taskIsDone(@RequestBody String name){
    Response response=taskService.taskIsDone(name);
    return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/getTaskI_Gave/{name}")
    public ResponseEntity<Task> getTaskI_Gave(@PathVariable String name){
    return ResponseEntity.ok(taskService.getTaskI_Gave(name));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTasks(){
        if (taskService.getAllTasks()!=null) {
            return ResponseEntity.ok(taskService.getAllTasks());
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("To get list of tasks not allowed for you.");
    }

    @GetMapping("/getAllMyTasks")
    public ResponseEntity<?> getMyAllTasks(){
        if (taskService.getMyTasks()!=null) {
            return ResponseEntity.ok(taskService.getMyTasks());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Currently you have not got any task");
    }

}
