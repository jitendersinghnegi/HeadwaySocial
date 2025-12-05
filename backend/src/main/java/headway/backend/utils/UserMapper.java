package headway.backend.utils;

import headway.backend.dto.UserResponseDTO;
import headway.backend.entity.user.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail())
                .roles(
                        user.getRoles().stream()
                                .map(r -> r.getRoleName().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
