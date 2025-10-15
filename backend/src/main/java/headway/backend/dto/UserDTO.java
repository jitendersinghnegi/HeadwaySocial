package headway.backend.dto;

import headway.backend.entity.user.Role;

public class UserDTO {

    public UserDTO(Long id, String email, String password, String userName, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
    }

    public UserDTO() {
    }
    private Long id;
    private String email;
    private String password;
    private String userName;
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                '}';
    }
}
