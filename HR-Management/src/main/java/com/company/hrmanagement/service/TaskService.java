package com.company.hrmanagement.service;

import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.dto.TaskDto;
import com.company.hrmanagement.entity.Task;
import com.company.hrmanagement.entity.User;
import com.company.hrmanagement.entity.enums.RoleType;
import com.company.hrmanagement.entity.enums.TaskType;
import com.company.hrmanagement.repository.RoleRepository;
import com.company.hrmanagement.repository.TaskRepository;
import com.company.hrmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TaskService {
private final AuthService authService;
private final UserRepository userRepository;
private final RoleRepository roleRepository;
private final TaskRepository taskRepository;
private  final AuthenticationManager authenticationManager;
private final JavaMailSender javaMailSender;
@Autowired
    public TaskService(AuthService authService, UserRepository userRepository, RoleRepository roleRepository, TaskRepository taskRepository, AuthenticationManager authenticationManager, JavaMailSender javaMailSender) {
        this.authService = authService;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
    this.authenticationManager = authenticationManager;
    this.javaMailSender = javaMailSender;

}
private static final long time=1000*60*60*24;

public Response addTask(TaskDto taskDto){
    Optional<User> optionalUser=userRepository.findByEmail(taskDto.getEmployeeUsername());
    if (optionalUser.isPresent()) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
                User user=(User) authentication.getPrincipal();
        if (optionalUser.get().getCompany().equals(user.getCompany())) {
            if (user.getAuthorities().equals(Collections.singleton(RoleType.MANAGER))&&optionalUser.get().getAuthorities().contains(Collections.singleton(RoleType.WORKER)) ||
                    user.getAuthorities().equals(Collections.singleton(RoleType.DIRECTOR))&&optionalUser.get().getAuthorities().contains(Collections.singleton(RoleType.WORKER))||
                                    user.getAuthorities().equals(Collections.singleton(RoleType.DIRECTOR))&&optionalUser.get().getAuthorities().contains(Collections.singleton(RoleType.MANAGER))
            ){
                Task task=new Task();
                task.setName(taskDto.getName());
                task.setCaption(taskDto.getCaption());
                task.setDeadline(new Timestamp(System.currentTimeMillis()+taskDto.getDeadline()*time));
                task.setUser(user);
                task.setTaskCurrentType(TaskType.NEW);
                task.setDone(false);
                task.setGivenBy(user.getEmail());
                taskRepository.save(task);
                authService.sendEmail(optionalUser.get().getEmail(),task.getId().toString(),"New task added");
                return new Response("Task successfully added. And task delivered to employee",true);
            }
        }
        return new Response("This employee`s company does not match ",false);
    }
    return new Response("This employee not found",false);
}


    public Task getMyTask(UUID id) {
    return taskRepository.findById(id).get();
    }

    public Response taskIsDone(String name){
    Optional<Task> task=taskRepository.findByName(name);
        if (task.isPresent()) {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            User user=(User) authentication.getPrincipal();
            if (task.get().getUser().getId().toString().equals(user.getId().toString())) {
                Task task1=task.get();
                task1.setDone(true);
                taskRepository.save(task1);
                authService.sendEmail(user.getEmail(),name,"Task is done");
            }
            return new Response("This task not given for you",false);
        }
        return new Response("This task not found",false);
    }

    public Task getTaskI_Gave(String name){
    return taskRepository.findByName(name).get();
    }

    public List<Task> getAllTasks(){
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    User user=(User) authentication.getPrincipal();
        if (Set.of(RoleType.DIRECTOR,RoleType.HR_MANAGER).contains(user.getAuthorities())) {
            return taskRepository.findAll();
        }
        return null;
    }

    public List<Task> getMyTasks(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        List<Task>tasks=taskRepository.findAllById(Collections.singleton(user.getId()));
        return tasks;
    }
}
