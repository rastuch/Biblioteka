package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPassword("haslo123");
        user.setRole("USER");
        user.setPhoneNumber("123456789");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        Optional<User> found = userRepo.findByEmail("test@example.com");
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void whenFindAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContainingOrRoleIsIn_thenReturnUsers() {
        List<String> roles = Arrays.asList("USER");
        Iterable<User> users = userRepo.findAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContainingOrRoleIsIn("Kowalski", roles);
        assertThat(users).hasSize(1).contains(user);
    }

    @Test
    public void whenFindAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining_thenReturnUsers() {
        Iterable<User> users = userRepo.findAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining("Kowalski");
        assertThat(users).hasSize(1).contains(user);
    }

    @Test
    public void whenFindAllByRoleIsIn_thenReturnUsers() {
        List<String> roles = Arrays.asList("USER");
        Iterable<User> users = userRepo.findAllByRoleIsIn(roles);
        assertThat(users).hasSize(1).contains(user);
    }
}
