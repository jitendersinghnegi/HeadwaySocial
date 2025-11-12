package headway.backend.service.impl;

import headway.backend.dto.MessageResponse;
import headway.backend.dto.SignupRequestDTO;
import headway.backend.dto.UserResponseDTO;
import headway.backend.entity.user.AppRole;
import headway.backend.entity.user.Role;
import headway.backend.entity.user.User;
import headway.backend.repo.RoleRepository;
import headway.backend.repo.UserRepository;
import headway.backend.service.AuditService;
import headway.backend.service.UserService;
import headway.backend.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    /**
     * @return
     */
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuditService auditService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    /**
     * @param registerRequest
     * @return
     */
    @Override
    public ResponseEntity<?> registerUser(SignupRequestDTO registerRequest) {
        if (userRepository.existsByUserName(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        auditService.recordAction(
                "User",
                user.getUserId().toString(),
                "Create",
                "Created User  : "+ user.getUserName()
        );
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
