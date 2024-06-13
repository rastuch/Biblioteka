package com.studia.biblioteka.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studia.biblioteka.NoSecurityConfig;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.NewCopy;
import com.studia.biblioteka.manager.BookManager;
import com.studia.biblioteka.manager.CopyManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CopyApi.class)
@Import(NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class CopyApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CopyManager copyManager;

    @MockBean
    private BookManager bookManager;

    @Autowired
    private ObjectMapper objectMapper;

    private Copy copy;
    private Book book;

    @Before
    public void setup() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        copy = new Copy();
        copy.setId(1L);
        copy.setBook(book);
        copy.setLocation("Library Shelf 1");
        copy.setStatus(CopyStatus.AVAILABLE);
    }

    @Test
    public void getById_whenFound() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.of(copy));

        mockMvc.perform(get("/api/copy").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(copy.getId()));
    }

    @Test
    public void getById_whenNotFound() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/copy").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllCopies() throws Exception {
        given(copyManager.findAll()).willReturn(Arrays.asList(copy));

        mockMvc.perform(get("/api/copy/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(copy.getId()));
    }

    @Test
    public void addCopy_success() throws Exception {
        when(bookManager.findById(1L)).thenReturn(Optional.of(book));
        when(copyManager.save(any(Copy.class))).thenReturn(copy);

        NewCopy newCopy = new NewCopy();
        newCopy.setBookId(1L);
        newCopy.setLocation("New Shelf");
        newCopy.setStatus(CopyStatus.AVAILABLE);

        mockMvc.perform(post("/api/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCopy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(CopyStatus.AVAILABLE.toString()));
    }


    @Test
    public void addCopy_bookNotFound() throws Exception {
        given(bookManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(copy)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCopy_whenExists() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.of(copy));
        given(copyManager.save(any(Copy.class))).willReturn(copy);

        mockMvc.perform(put("/api/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(copy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Library Shelf 1"));
    }

    @Test
    public void updateCopy_whenNotFound() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(copy)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCopy_whenExists() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.of(copy));
        willDoNothing().given(copyManager).delete(1L);

        mockMvc.perform(delete("/api/copy").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCopy_whenNotFound() throws Exception {
        given(copyManager.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/copy").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCopyStatuses() throws Exception {
        mockMvc.perform(get("/api/copy/statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(CopyStatus.AVAILABLE.toString()));
    }

    @Test
    public void getAllCopiesByBookId() throws Exception {
        given(copyManager.findAllBookCopies(1L)).willReturn(Arrays.asList(copy));

        mockMvc.perform(get("/api/copy/allByBookId").param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(copy.getId()));
    }
}
