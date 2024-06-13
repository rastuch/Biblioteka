package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Fine;
import com.studia.biblioteka.dao.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FineRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FineRepo fineRepo;

    private User user;
    private Fine fine1, fine2;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        entityManager.persist(user);

        fine1 = Fine.builder()
                .user(user)
                .amount(new BigDecimal("50.00"))
                .reason("Late return")
                .build();
        fine2 = Fine.builder()
                .user(user)
                .amount(new BigDecimal("75.00"))
                .reason("Damaged book")
                .build();
        entityManager.persist(fine1);
        entityManager.persist(fine2);
        entityManager.flush();
    }

    @Test
    public void whenFindAllByUserId_thenReturnFines() {
        Iterable<Fine> fines = fineRepo.findAllByUserId(user.getId());
        assertThat(fines).containsExactlyInAnyOrder(fine1, fine2);
    }
}
