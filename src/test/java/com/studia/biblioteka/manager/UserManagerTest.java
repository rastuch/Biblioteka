package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.UserRepo;
import com.studia.biblioteka.dao.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserManagerTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserManager userManager;

    private User user;

    @Before
    public void setUp() {
        user = new User(1L, "admin", "", "admin@admin.pl", "ADMIN", "admin", "0000000", null, null, null);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userRepo.findByEmail("admin@admin.pl")).thenReturn(Optional.of(user));
    }

    @Test
    public void whenFindById_thenReturnUser() {
        Optional<User> found = userManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    public void whenFindAll_thenReturnAllUsers() {
        when(userRepo.findAll()).thenReturn(Arrays.asList(user));
        Iterable<User> users = userManager.findAll(null, null);
        assertTrue(users.iterator().hasNext());
        assertEquals(user, users.iterator().next());
    }

    @Test
    public void whenSave_thenReturnSavedUser() {
        User newUser = new User(2L, "test", "", "test@test.com", "USER", "test", "1111111", null, null, null);
        when(userRepo.save(any(User.class))).thenReturn(newUser);
        User savedUser = userManager.save(newUser);
        assertNotNull(savedUser);
        assertEquals("test@test.com", savedUser.getEmail());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        userManager.delete(1L);
        verify(userRepo).deleteById(1L);
    }

    @Test
    public void whenAuthenticate_thenReturnUser() {
        Optional<User> authenticatedUser = userManager.authenticate("admin@admin.pl", "admin");
        assertTrue(authenticatedUser.isPresent());
        assertEquals(user.getEmail(), authenticatedUser.get().getEmail());
    }

    @Test
    public void whenCheckEmailExistence_thenReturnTrue() {
        Boolean exists = userManager.isEmailExist("admin@admin.pl");
        assertTrue(exists);
    }
}
