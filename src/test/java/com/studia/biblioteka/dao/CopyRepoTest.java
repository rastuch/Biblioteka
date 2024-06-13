package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CopyRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CopyRepo copyRepo;

    private Book book;
    private Copy copy1, copy2;

    @Before
    public void setUp() {
        book = Book.builder()
                .title("Clean Code")
                .authors("Robert C. Martin")
                .category("Programming")
                .build();
        entityManager.persist(book);

        copy1 = Copy.builder()
                .book(book)
                .status(CopyStatus.AVAILABLE)
                .location("Shelf A")
                .build();
        copy2 = Copy.builder()
                .book(book)
                .status(CopyStatus.BORROWED)
                .location("Shelf B")
                .build();
        entityManager.persist(copy1);
        entityManager.persist(copy2);
        entityManager.flush();
    }

    @Test
    public void whenFindAllByBookId_thenReturnCopies() {
        Iterable<Copy> copies = copyRepo.findAllByBook_Id(book.getId());
        assertThat(copies).containsExactlyInAnyOrder(copy1, copy2);
    }

    @Test
    public void whenDeleteAllByBookId_thenNoCopiesShouldExist() {
        copyRepo.deleteAllByBook_Id(book.getId());
        entityManager.flush();  // Flush to ensure all operations are completed

        Iterable<Copy> copies = copyRepo.findAllByBook_Id(book.getId());
        assertThat(copies).isEmpty();
    }
}
