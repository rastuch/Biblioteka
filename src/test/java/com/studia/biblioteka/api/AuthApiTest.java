package com.studia.biblioteka.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studia.biblioteka.dto.UserLogin;
import com.studia.biblioteka.dto.UserRegister;
import com.studia.biblioteka.manager.UserManager;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.secure.TokenStoreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManager userManager;

    @MockBean
    private TokenStoreService tokenStoreService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole("USER");

        when(userManager.authenticate("user@example.com", "password")).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser
    public void loginTest() throws Exception {
        UserLogin login = new UserLogin();
        login.setEmail("user@example.com");
        login.setPassword("password");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @WithMockUser(username = "1", authorities = {"USER"})
    public void getMeInfoTest() throws Exception {
        // Assuming a method in userManager to fetch by username, and correct token setup if needed
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("admin@admin.pl");
        mockUser.setRole("ADMIN");
        Algorithm algorithm = Algorithm.HMAC256("secret");


        Date now = new Date();
        var expiryDate  = new Date(now.getTime() + (long) 360 * 3600 * 1000 * 24);
        String token = JWT.create()
                .withSubject(mockUser.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withClaim("userId", mockUser.getId())
                .withClaim("role", mockUser.getRole())
                .sign(algorithm);

        String authHeader = "Bearer " + token;

        // Simulate the token being valid and stored prior to logout

        doNothing().when(tokenStoreService).storeToken(token,expiryDate );
        when(tokenStoreService.isTokenValid(token)).thenReturn(true);
        mockMvc.perform(get("/api/me")
                        .header("Authorization", authHeader) // Ensure this token is recognized by your security filters
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    public void registerUserTest() throws Exception {
        UserRegister userDto = new UserRegister();
        userDto.setEmail("new@example.com");
        userDto.setPassword("newpassword");
        userDto.setFirstName("New");
        userDto.setLastName("User");
        userDto.setPhoneNumber("1234567890");

        when(userManager.isEmailExist(userDto.getEmail())).thenReturn(false);
        when(userManager.save(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void logoutTest() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5wbCIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTc0OTI5MTQ4MiwiaWF0IjoxNzE4MTg3NDgyLCJ1c2VySWQiOjF9.2fmyMDMxpZq3HNdxaRHAD262mwaF8lxmG9exmK_IEso";
        String authHeader = "Bearer " + token;

        // Simulate the token being valid and stored prior to logout
        doNothing().when(tokenStoreService).storeToken(token, new Date());  // Assuming the storeToken method signature
        when(tokenStoreService.isTokenValid(token)).thenReturn(true);

        mockMvc.perform(get("/api/logout")
                        .header("Authorization", authHeader)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));

        // Verify that the tokenStoreService has been called to invalidate the token
        verify(tokenStoreService).invalidateToken(token);
    }
}
