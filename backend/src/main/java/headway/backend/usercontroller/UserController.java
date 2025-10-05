package headway.backend.usercontroller;

import headway.backend.dto.UserDTO;
import headway.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")

public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO){
        String id = userService.registerUser(userDTO);
        return id;
    }

}
