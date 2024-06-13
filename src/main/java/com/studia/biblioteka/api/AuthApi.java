package com.studia.biblioteka.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.studia.biblioteka.dto.JwtResponse;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.dto.UserLogin;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dto.UserRegister;
import com.studia.biblioteka.manager.UserManager;
import com.studia.biblioteka.secure.TokenStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.studia.biblioteka.dto.ErrorResponse;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthApi {
    private static final Logger logger = LoggerFactory.getLogger(AuthApi.class);

    @Autowired
    private UserManager userManager;

    @Value("${jwt.expiration.days}")
    private int expirationDays;

    @Autowired
    private TokenStoreService tokenStoreService;

    @Operation(summary = "Login every user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login, returns JWT token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Login failed: Invalid username or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody UserLogin login) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (long) expirationDays * 3600 * 1000 * 24);
        logger.info(login.getEmail());
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

                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setToken(token);

                    return ResponseEntity.ok((Object) jwtResponse);
                })
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Login Failed: Invalid username or password");
                    return ResponseEntity.status(401).body(errorResponse);
                });
    }

    @Operation(summary = "Information about logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of user information",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/me")
    public Optional<User> getMeInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId = (long) auth.getPrincipal();
        return userManager.findById(userId);
    }

    @Operation(summary = "Register new user with role USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister userDto) {
        try {
            if (userManager.isEmailExist(userDto.getEmail())) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage("Email Already Exists");
                return ResponseEntity.status(409).body(errorResponse);
            }
            var users = userManager.findAll(null, null);

            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setRole("USER");

            User registeredUser = userManager.save(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Logout the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            try {
                tokenStoreService.invalidateToken(token);
                SuccessResponse successResponse = new SuccessResponse();
                successResponse.setMessage("Logged out successfully");
                return ResponseEntity.ok(successResponse);
            } catch (Exception e) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage(e.getMessage());
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid token");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
