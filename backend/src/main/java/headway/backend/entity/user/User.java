package headway.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
// once normal code works implement UserDetails from  org.springframework.security.core.userdetails.UserDetails
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false,name = "email")
    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Date resetTokenExpiry;

    @Column(nullable = false,name="password")
    private String password;
    @Column(unique = true,nullable = false,name="username")
    private String userName;
    @Setter
    @Getter
    @ManyToMany(cascade = {CascadeType.MERGE},
    fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="user_address",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name="address_id"))

    private List<Address> addresses = new ArrayList<>();

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
