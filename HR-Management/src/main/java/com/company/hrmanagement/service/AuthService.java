package com.company.hrmanagement.service;

import com.company.hrmanagement.dto.RegisterDto;
import com.company.hrmanagement.dto.LoginDto;
import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.entity.*;
import com.company.hrmanagement.entity.enums.RoleType;
import com.company.hrmanagement.repository.*;
import com.company.hrmanagement.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private CompanyRepository companyRepository;
    private  RoleRepository roleRepository;
    private  PasswordEncoder passwordEncoder;
    private  AuthenticationManager authenticationManager;
    private  JwtProvider jwtProvider;
    private  JavaMailSender javaMailSender;
   private UserRepository userRepository;
   private TurnstileRepository turnstileRepository;
@Autowired
    public AuthService(
        CompanyRepository companyRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtProvider jwtProvider,
        JavaMailSender javaMailSender, UserRepository userRepository, TurnstileRepository turnstileRepository) {
    this.companyRepository = companyRepository;
    this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.javaMailSender = javaMailSender;
    this.userRepository = userRepository;
    this.turnstileRepository = turnstileRepository;
}
    public Response addDirector(RegisterDto registerDto){
    Optional<User> directorOptional= userRepository.findByCompany_NameAndCompany_Address(registerDto.getCompany().getName(), registerDto.getCompany().getAddress());
    if (directorOptional.isPresent()&& roleRepository.findByRoles(RoleType.DIRECTOR).isPresent()) {
        if (directorOptional.get().getRoles().contains(roleRepository.findByRoles(RoleType.DIRECTOR).get())) {
                 return new Response("This company has their own director !!!",false);


        }
    }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new Response("This email already exists",false);
        }
        Optional<Company>company= companyRepository.findByNameAndAddress(registerDto.getCompany().getName(), registerDto.getCompany().getAddress());
    if (company.isPresent()) {
        User user =new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setCompany(company.get());
        user.setEmailCode(UUID.randomUUID().toString());
        user.setRoles(Collections.singleton(roleRepository.findByRoles(RoleType.DIRECTOR).get()));
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode(),"Verification email for director");
        return new Response("You successfully registered !!!\n To activate your account, please verify your email.",true);
    }
    else{
        return new Response("This company does not exists",false);
    }

}

public Response verifyEmail(String email,String email_code){
    Optional<User> directorOptional= userRepository.findByEmailAndEmailCode(email,email_code);
    if (directorOptional.isPresent()) {
        User user =directorOptional.get();
        if (user.getEmailCode()==null) {
            return new Response("This account already verified",false);
        }
        user.setEnable(true);
        user.setEmailCode(null);
        userRepository.save(user);
        return new Response("Your email verified",true);
    }
    return new Response("This account not found",false);
}


    public Response login(LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getRoles());
            return new  Response("This is token",true,token);
        }catch (BadCredentialsException e){
            return new Response("Username or password is incorrect",false);
        }

    }

    public Boolean sendEmail(String email,String code,String emailSubject){
        try
        {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom("abs@gmail.com");
            message.setTo(email);
            message.setSubject("Verification Email");
            message.setText(findTypeOfEmail(emailSubject,email,code));
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
return false;

        }
}

public Response addHR_Manager(RegisterDto employeeDto){
    Optional<Company> companyOptional = companyRepository.findByNameAndAddress(employeeDto.getCompany().getName(), employeeDto.getCompany().getAddress());
    if (companyOptional.isPresent()) {
        Authentication currentDirector= SecurityContextHolder.getContext().getAuthentication();

        if (currentDirector.getAuthorities().contains(Collections.singleton(RoleType.DIRECTOR))) {
            return new Response("HR_MANAGERS can only be added by DIRECTORS", false);
        }
        User director =(User) currentDirector.getPrincipal();
        if (director.getCompany().equals(companyOptional.get())) {
            User user =add(employeeDto, companyOptional);
            user.setRoles(Collections.singleton(roleRepository.findByRoles(RoleType.HR_MANAGER).get()));
            user.setEmailCode(UUID.randomUUID().toString());
            userRepository.save(user);
            sendEmail(user.getEmail(), user.getEmailCode(),"Verification email");
            return new Response("HR_MANAGER successfully added !!!",true);
        }
        return new Response("You can not add employee. Because you are not director of this company",false);
    }
    return new Response("Company with this name and address not found",false);
}

    public Response addManager(RegisterDto employeeDto){
        Optional<Company> companyOptional = companyRepository.findByNameAndAddress(employeeDto.getCompany().getName(), employeeDto.getCompany().getAddress());
        if (companyOptional.isPresent()) {
            Authentication currentDirector= SecurityContextHolder.getContext().getAuthentication();

            if (currentDirector.getAuthorities().contains(Collections.singleton(RoleType.DIRECTOR))) {
                return new Response("HR_MANAGERS can only be added by DIRECTORS", false);
            }
            User director =(User) currentDirector.getPrincipal();
            if (director.getCompany().equals(companyOptional.get())) {
                User user =add(employeeDto, companyOptional);
                user.setRoles(Collections.singleton(roleRepository.findByRoles(RoleType.MANAGER).get()));
                user.setEmailCode(UUID.randomUUID().toString());
                userRepository.save(user);
                sendEmail(user.getEmail(), user.getEmailCode(),"Verification email");
                return new Response("Manager successfully added !!!",true);
            }
            return new Response("You can not add employee. Because you are not director of this company",false);
        }
        return new Response("Company with this name and address not found",false);
    }


public Response verifyEmployeeEmail(String email,String code){
    Optional<User> optionalEmployee= userRepository.findByEmailAndEmailCode(email,code);
    if (optionalEmployee.isPresent()) {
        User user =optionalEmployee.get();
        user.setEnable(true);
        Turnstile turnstile=addTurnstile();
        if (turnstile!=null) {
            turnstile.setUser(user);
            user.setEmailCode(null);
            turnstile.setAvailable(false);
            userRepository.save(user);
            sendEmail(email,code,"Update your password");
        }else{
            return new Response("Email verification was not achieved. Because suitable turnstile not found in database. Later tyr again",false);
        }
        return new Response("Email successfully verified. New Turnstile added for you. And your turnstile is ready to use !!!",true);
    }
    return new Response("Hr_Manager with this email and emailCode not found",false);
}

public Response updatePasswordByEmployee(String email,String password){
    Optional<User>optionalEmployee= userRepository.findByEmail(email);
    if (optionalEmployee.isPresent()) {
        User user =optionalEmployee.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new Response("Password updated successfully. You can log into the system with this email and password",true);
    }
    return new Response("User with this email not found",false);
}

public Response addWorker(RegisterDto employeeDto){
    Optional<Company> optionalCompany= companyRepository.findByNameAndAddress(employeeDto.getCompany().getName(),employeeDto.getCompany().getAddress());
    if (optionalCompany.isPresent()) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (Collections.singleton(RoleType.HR_MANAGER).contains(authentication.getAuthorities())) {
return new Response("Employees can be added only by HR_MANAGERS",false);
        }
        User hr_manager=(User) authentication.getPrincipal();
        if (hr_manager.getCompany().equals(optionalCompany.get())) {
            User user = add(employeeDto, optionalCompany);
            user.setRoles(Collections.singleton(roleRepository.findByRoles(RoleType.WORKER).get()));
            user.setEmailCode(UUID.randomUUID().toString());
            userRepository.save(user);
            sendEmail(user.getEmail(), user.getEmailCode(),"Verification email");
            return new Response("HR_MANAGER successfully added !!!",true);
        }
        return new Response("You can not add employee. Because you are not the HR_MANAGER of this company",false);


    }
    return new Response("Company with this name and address not found",false);
}

    public User add(RegisterDto employeeDto, Optional<Company> optionalCompany) {
        User user =new User();
        user.setFirstName(employeeDto.getFirstName());
        user.setLastName(employeeDto.getLastName());
        user.setEmail(employeeDto.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        user.setCompany(optionalCompany.get());
        if(employeeDto.getSalary()!=null){
            Salary salary=employeeDto.getSalary();
            salary.setUser(user);
        }
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User>director= userRepository.findByEmail(username);
        return director.orElseThrow(()->new UsernameNotFoundException("This user not found"));
    }

    public Turnstile addTurnstile(){
        List<Turnstile> turnstiles=turnstileRepository.findAll();
        for (Turnstile turnstile : turnstiles) {
            if (turnstile.isAvailable()) {
                return turnstile;
            }
        }
        return null;
    }

    public String findTypeOfEmail(String emailSubject,String email,String code){
        return switch (emailSubject) {
            case "Verification email" -> "<a href='http://localhost:8080/api/auth/verifyEmail?code=" + code + "&email=" + email + "'>Verify your email</a>";
            case "Verification email for director" -> "<a href='http://localhost:8080/api/auth/verify?code=" + code + "&email=" + email + "'>Verify your email</a>";
            case "Update your password" -> "<a href='http://localhost:8080/api/auth/updatePassword'>Update your password</a>";
            case "Task is done" -> "<a href='http://localhost:8080/api/getTaskI_Gave/"+code+"'>Task you gave is done by employee</a>";
            case "New task added" -> "<a href='http://localhost:8080/api/myTask/"+code+"'>You have new Task</a>";
            default -> null;
        };
    }

}
