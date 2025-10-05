package headway.backend.service;

import headway.backend.dto.UserDTO;

public interface UserService {
    String registerUser(UserDTO userDTO);
}
