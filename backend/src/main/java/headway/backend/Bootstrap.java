package headway.backend;

import headway.backend.entity.user.Role;
import headway.backend.entity.user.User;
import headway.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class Bootstrap {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    CommandLineRunner seed(UserRepository users){
       return args -> {
           if (users.count()==0){
               User user = new User();
               user.setRole(Role.ADMIN);
               user.setPassword(this.passwordEncoder.encode("LKMKC"));
               user.setEmail("laudalassan@lyreco.com");
               user.setUserName("GregChutiya");
               users.save(user);
              /* users.save(User.builder()
                       .userName("admin")
                       .email("jeet.sujanian@gmail.com")
                       .password("LKMKC")
                       .role(Role.ADMIN)
                       .build());*/
           }
       };
    }
}
