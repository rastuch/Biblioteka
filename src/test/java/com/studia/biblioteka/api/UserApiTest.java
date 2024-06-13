package com.studia.biblioteka.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studia.biblioteka.NoSecurityConfig;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.manager.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@WebMvcTest(UserApi.class)
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManager userManager;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
    }

    @Test
    void getByIdSuccess() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(get("/api/users?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getByIdNotFound() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users?id=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers() throws Exception {
        given(userManager.findAll(null, null)).willReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void addUserSuccess() throws Exception {
        given(userManager.save(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUserNotFound() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserSuccess() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.of(user));
        given(userManager.save(any(User.class))).willReturn(user);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserSuccess() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        given(userManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/users?id=1"))
                .andExpect(status().isNotFound());
    }
}
