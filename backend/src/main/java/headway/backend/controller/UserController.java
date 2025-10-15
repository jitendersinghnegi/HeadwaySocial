package headway.backend.controller;

import headway.backend.dto.UserDTO;
import headway.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")

public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO){
        String id = userService.registerUser(userDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

}
