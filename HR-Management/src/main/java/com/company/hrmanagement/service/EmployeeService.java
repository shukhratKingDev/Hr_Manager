package com.company.hrmanagement.service;

import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.dto.SalaryDto;
import com.company.hrmanagement.entity.Salary;
import com.company.hrmanagement.entity.Turnstile;
import com.company.hrmanagement.entity.User;
import com.company.hrmanagement.repository.SalaryRepository;
import com.company.hrmanagement.repository.TurnstileRepository;
import com.company.hrmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.hrmanagement.entity.enums.RoleType.*;

@Service
public class EmployeeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private TurnstileRepository turnstileRepository;

    public List<User> getAllEmployees(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user1=(User) authentication.getPrincipal();
        if (Set.of(HR_MANAGER,DIRECTOR).contains(user1.getAuthorities())) {


        List<User> users=userRepository.findAll();
        return users.stream().filter(user ->  user.getAuthorities().contains(Set.of(MANAGER, WORKER))).collect(Collectors.toList());
    }
        return null;
    }

    public Response addSalaryForEmployees(SalaryDto salaryDto){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        if (Objects.equals(DIRECTOR, user.getAuthorities())) {
            Optional<User> optionalUser = userRepository.findByEmail(salaryDto.getEmail());
            if (optionalUser.isPresent()) {
                if (optionalUser.get().getCompany().equals(user.getCompany())) {
                    if (!optionalUser.get().getAuthorities().equals(Collections.singleton(DIRECTOR))) {
                        Salary salary=new Salary();
                        salary.setAmount(salaryDto.getAmount());
                        salary.setUser(user);
                        salaryRepository.save(salary);
                        return new Response("Salary successfully added to the employee",true);
                    }
                    return new Response("You can not add salary for yourself",false);
                }
                return new Response("You can not add salary for this employee. Because he/she is not your employee",false);
            }
            return new Response("Employee with this email not found",false);



        }
        return new Response("You can not add salary for employee",false);
    }

    public Response inside(String username){
        Optional<User> user=userRepository.findByEmail(username);
        if (user.isPresent()) {
            Optional<Turnstile> optionalTurnstile = turnstileRepository.findByUser_Id(user.get().getId());
            if (optionalTurnstile.isPresent()){
                optionalTurnstile.get().setInside(true);
                optionalTurnstile.get().setEnteredAt(Timestamp.valueOf(LocalDateTime.now()));
                return new Response("Welcome !!!",true);
            }
            return new Response("A turnstile belongs to you not found",false);
        }
        return new Response("The user with this username not found",false);
    }

    public Response outside(String username){
        Optional<User> user=userRepository.findByEmail(username);
        if (user.isPresent()) {
            Optional<Turnstile> optionalTurnstile = turnstileRepository.findByUser_Id(user.get().getId());
            if (optionalTurnstile.isPresent()){
                optionalTurnstile.get().setInside(false);
                optionalTurnstile.get().setLeftAt(Timestamp.valueOf(LocalDateTime.now()));
                return new Response("Good bye !!!",true);
            }
            return new Response("A turnstile belongs to you not found",false);
        }
        return new Response("The user with this username not found",false);
    }
}
