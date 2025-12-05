package headway.backend.service;

import headway.backend.dto.SignupRequestDTO;
import headway.backend.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO registerRequest);
}
