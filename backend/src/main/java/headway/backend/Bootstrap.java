package headway.backend;

import headway.backend.user.Role;
import headway.backend.user.User;
import headway.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Bootstrap {
    @Bean
    CommandLineRunner seed(UserRepository users){
       return args -> {
           if (users.count()==0){
               users.save(User.builder()
                       .userName("admin")
                       .email("jeet.sujanian@gmail.com")
                       .password("LKMKC")
                       .role(Role.ADMIN)
                       .build());
           }
       };
    }
}
