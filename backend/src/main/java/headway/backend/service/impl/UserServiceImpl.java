package headway.backend.service.impl;

import headway.backend.dto.UserDTO;
import headway.backend.entity.user.User;
import headway.backend.repo.UserRepository;
import headway.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public String registerUser(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setId(userDTO.getId());
        user.setRole(userDTO.getRole());
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        /*
        User user = new User(userDTO.getId(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getUserName(),
                userDTO.getRole());*/
        userRepo.save(user);
        return user.getId().toString();
    }
}
