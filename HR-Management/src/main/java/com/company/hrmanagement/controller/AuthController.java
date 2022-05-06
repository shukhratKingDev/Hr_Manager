package com.company.hrmanagement.controller;

import com.company.hrmanagement.dto.LoginDto;
import com.company.hrmanagement.dto.RegisterDto;
import com.company.hrmanagement.dto.Response;
import com.company.hrmanagement.dto.UpdateDto;
import com.company.hrmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
@Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
}

    @PostMapping("/register")
    public ResponseEntity<?> addDirector( @RequestBody RegisterDto director){
    Response response= authService.addDirector(director);
    return  ResponseEntity.status(response.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(response);
}

    @PostMapping("/addManager")
    public ResponseEntity<?> addManager( @RequestBody RegisterDto manager){
        Response response= authService.addManager(manager);
        return  ResponseEntity.status(response.isSuccess()? HttpStatus.CREATED: HttpStatus.CONFLICT).body(response);
    }

@PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginDto loginDto){
    Response response= authService.login(loginDto);
    return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.NOT_FOUND).body(response);
}

    @GetMapping("/verify")// this path is for director
    public ResponseEntity<Response> verify(@RequestParam String email,@RequestParam String code) {
        Response response = authService.verifyEmail(email, code);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }
@GetMapping("/verifyEmail")// this path is for director
    public ResponseEntity<Response> verifyEmail(@RequestParam String email,@RequestParam String code) {
    Response response = authService.verifyEmployeeEmail(email, code);
    return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
}

@PostMapping("/addHr_Manager")
    public ResponseEntity<Response> addHr_Manager(@RequestBody RegisterDto manager){
    Response response=authService.addHR_Manager(manager);
    return ResponseEntity.status(response.isSuccess()?HttpStatus.CREATED:HttpStatus.CONFLICT).body(response);
}

    @PostMapping("/addWorker")
    public ResponseEntity<Response> addWorker(@RequestBody RegisterDto worker){
        Response response=authService.addWorker(worker);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.CREATED:HttpStatus.CONFLICT).body(response);
    }




@PostMapping("/updatePassword")
    public ResponseEntity<Response> updatePasswordByEmployee(@RequestBody UpdateDto updateDto){
    String p=updateDto.getPassword();
    String e=updateDto.getEmail();
    Response response=authService.updatePasswordByEmployee(updateDto.getEmail(),updateDto.getPassword());
    return ResponseEntity.status(response.isSuccess()?HttpStatus.ACCEPTED:HttpStatus.NOT_FOUND).body(response);
}
}
