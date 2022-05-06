package com.company.hrmanagement.service;

import com.company.hrmanagement.dto.ReportDto;
import com.company.hrmanagement.entity.User;
import com.company.hrmanagement.entity.enums.RoleType;
import com.company.hrmanagement.repository.SalaryRepository;
import com.company.hrmanagement.repository.TaskRepository;
import com.company.hrmanagement.repository.TurnstileRepository;
import com.company.hrmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private SalaryRepository salaryRepository;
    private TurnstileRepository turnstileRepository;
@Autowired
    public ReportService(UserRepository userRepository, TaskRepository taskRepository, SalaryRepository salaryRepository, TurnstileRepository turnstileRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.salaryRepository = salaryRepository;
    this.turnstileRepository = turnstileRepository;
}

    public List<ReportDto> getReportAboutEmployees(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User)authentication.getPrincipal();
        if (Set.of(RoleType.DIRECTOR,RoleType.HR_MANAGER).contains(currentUser.getRoles())) {
            ReportDto reportDto = new ReportDto();
            List<User> userList = userRepository.findAll();
            userList = userList.stream().filter(user -> user.getRoles().contains(Set.of(RoleType.MANAGER, RoleType.WORKER))).collect(Collectors.toList());
            List<ReportDto> reportDtoList = new ArrayList<>();
            if (!userList.isEmpty()) {
                for (User user : userList) {
                    reportDto.setEmployee(user);
                    reportDto.setTask(taskRepository.findById(user.getId()).get());
                    reportDto.setSalary(salaryRepository.findById(user.getId()).get());
                    reportDto.setTurnstile(turnstileRepository.findById(user.getId()).get());
                }
                return new ArrayList<>();
            }
        }
        return null;
    }

}
