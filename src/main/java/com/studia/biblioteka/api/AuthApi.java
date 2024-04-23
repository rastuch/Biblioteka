package com.studia.biblioteka.api;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.studia.biblioteka.dto.UserLogin;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dto.UserRegister;
import com.studia.biblioteka.manager.UserManager;
import com.studia.biblioteka.secure.TokenStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthApi{

    @Autowired
    private UserManager userManager;


    @Value("${jwt.expiration.days}")
    private int expirationDays;

    @Autowired
    private TokenStoreService tokenStoreService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody UserLogin login) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (long) expirationDays * 3600 * 1000 * 24);

        return userManager.authenticate(login.getEmail(), login.getPassword())
                .map(user -> {
                    System.out.println(user.getEmail());
                    String role = user.getRole();
                    Algorithm algorithm = Algorithm.HMAC256("secret");
                    String token = JWT.create()
                            .withSubject(user.getEmail())
                            .withIssuedAt(now)
                            .withExpiresAt(expiryDate)
                            .withClaim("userId", user.getId())
                            .withClaim("role", role)
                            .sign(algorithm);
                    tokenStoreService.storeToken(token, expiryDate);
                    return ResponseEntity.ok(token);
                })
                .orElseGet(() -> ResponseEntity.status(401).body("Login Failed: Invalid username or password"));
    }

    @GetMapping("/api/me")
    public Optional<User> getMeInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId = (long) auth.getPrincipal();
        return userManager.findById(userId);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister userDto) {
        try {
            if(userManager.isEmailExist(userDto.getEmail())){
                return ResponseEntity.status(409).body("Email Already Exists");
            }

            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setRole("USER");

            User registeredUser = userManager.save(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            try {
                tokenStoreService.invalidateToken(token);
                return ResponseEntity.ok("Logged out successfully");
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }


}