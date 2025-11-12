package headway.backend.controller;

import headway.backend.dto.MessageResponse;
import headway.backend.dto.SignupRequestDTO;
import headway.backend.dto.UserResponseDTO;
import headway.backend.entity.user.AppRole;
import headway.backend.entity.user.Role;
import headway.backend.entity.user.User;
import headway.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/viewall")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO registerRequest) {
        return userService.registerUser(registerRequest);
    }
}
