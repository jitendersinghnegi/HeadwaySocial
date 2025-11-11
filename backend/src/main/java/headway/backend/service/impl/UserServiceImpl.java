package headway.backend.service.impl;

import headway.backend.dto.UserResponseDTO;
import headway.backend.repo.UserRepository;
import headway.backend.service.UserService;
import headway.backend.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    /**
     * @return
     */
    @Autowired
    UserRepository userRepository;
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
