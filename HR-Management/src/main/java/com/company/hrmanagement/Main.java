package com.company.hrmanagement;

import com.company.hrmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {
    private static UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
//
        System.out.println(passwordEncoder.encode("12012002"));
//        System.out.println(userRepository.findByEmailAndEmailCode("shukhratDev1201@gmail.com", "085f59b4-c5f1-4bf4-a161-1cf12bd3b7bb")
    }
}
