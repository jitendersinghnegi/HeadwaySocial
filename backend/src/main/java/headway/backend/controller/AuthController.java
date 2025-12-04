package headway.backend.controller;

import headway.backend.dto.LoginRequestDTO;
import headway.backend.dto.LoginResponseDTO;
import headway.backend.dto.MessageResponse;
import headway.backend.dto.SignupRequestDTO;
import headway.backend.entity.user.AppRole;
import headway.backend.entity.user.Role;
import headway.backend.entity.user.User;
import headway.backend.repo.RoleRepository;
import headway.backend.repo.UserRepository;
import headway.backend.security.jwt.JwtUtils;
import headway.backend.security.services.UserDetailsImpl;
import headway.backend.service.AuditService;
import headway.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuditService auditService;

    @Autowired
    EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponseDTO response = new LoginResponseDTO(userDetails.getId()
                 ,userDetails.getUsername(), roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(response);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null){
            return authentication.getName();
        }else{
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponseDTO response = new LoginResponseDTO(userDetails.getId(),
                userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No user found with this email.");
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 1000 * 60 * 30)); // 30 min expiry
        userRepository.save(user);

        String resetUrl = frontendUrl + "/en/pms/reset-password?token=" + token;

        emailService.sendForgotPasswordEmail(
                user.getEmail(),
                "Password Reset",
                "Click the link to reset your password:\n\n" + resetUrl
        );

        return ResponseEntity.ok("Reset link sent to email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String token = request.get("token");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid reset token."
                    ));
        }

        if (user.getResetTokenExpiry().before(new Date())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Reset link has expired."
                    ));
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Password successfully updated."
                )
        );
    }

}
