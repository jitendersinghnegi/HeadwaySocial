package headway.backend;

import headway.backend.entity.user.AppRole;
import headway.backend.entity.user.Role;
import headway.backend.entity.user.User;
import headway.backend.repo.RoleRepository;
import headway.backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
public class Bootstrap {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {
            System.out.println("🚀 Bootstrapping roles and users...");

            // 1️Ensure all roles from AppRole exist
            for (AppRole appRole : AppRole.values()) {
                roleRepository.findByRoleName(appRole)
                        .orElseGet(() -> {
                            Role role = Role.builder().roleName(appRole).build();
                            roleRepository.save(role);
                            System.out.println("Created role: " + appRole);
                            return role;
                        });
            }

            // Fetch roles for assigning to users
            Role roleUser = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            Role roleManager = roleRepository.findByRoleName(AppRole.ROLE_MANAGER)
                    .orElseThrow(() -> new RuntimeException("ROLE_MANAGER not found"));
            Role roleAdmin = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            // Create 3 dummy users only if DB is empty
            if (userRepository.count() == 0) {
                User user1 = User.builder()
                        .userName("admin")
                        .email("admin@headway.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .roles(new HashSet<>(Set.of(roleAdmin)))
                        .build();

                User user2 = User.builder()
                        .userName("manager")
                        .email("manager@headway.com")
                        .password(passwordEncoder.encode("Manager@123"))
                        .roles(new HashSet<>(Set.of(roleManager)))
                        .build();

                User user3 = User.builder()
                        .userName("john_doe")
                        .email("user@headway.com")
                        .password(passwordEncoder.encode("User@123"))
                        .roles(new HashSet<>(Set.of(roleUser)))
                        .build();

                userRepository.saveAll(List.of(user1, user2, user3));
                System.out.println("Created 3 dummy users.");
            } else {
                System.out.println("Users already exist, skipping bootstrap.");
            }

            System.out.println("Bootstrap completed successfully.");
        };
    }

}
