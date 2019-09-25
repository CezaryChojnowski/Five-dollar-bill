package com.User;

import com.error.RecordExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PropertySource("classpath:messages.properties")
public class UserRestController {

    private final UserDAO userDAO;
    private final Environment env;

    @PostMapping
    public ResponseEntity registerUserAccount(@Valid @RequestBody User user) {
        if (userDAO.isEmailExists(user.getEmail())) {
            throw new RecordExistsException(env.getProperty("recordExists") + " " + user.getEmail());
        } else {
            userDAO.createUser(user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail());
            return ResponseEntity.ok(userDAO.convertToDto(user));
        }
    }

    @GetMapping
    public ResponseEntity getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userDAO.findUserByEmail(email));
    }
}
