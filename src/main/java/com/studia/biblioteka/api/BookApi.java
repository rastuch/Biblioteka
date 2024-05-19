package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.AuthorRepo;
import com.studia.biblioteka.dao.BookRepo;
import com.studia.biblioteka.dao.CategoryRepo;
import com.studia.biblioteka.dao.entity.Author;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Category;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewBook;
import com.studia.biblioteka.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/book")
public class BookApi {
    private BookRepo books;
    private CategoryRepo categories;
    private AuthorRepo authors;

    @Autowired
    public BookApi(BookRepo books, CategoryRepo categories, AuthorRepo authors) {
        this.books = books;
        this.categories = categories;
        this.authors = authors;
    }

    @Operation(summary = "Get book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getById(@RequestParam long id) {
        return books.findById(id)
                .map(book -> ResponseEntity.ok((Object) book))
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Book not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all books",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book[].class)))
    })
    @GetMapping("/all")
    public Iterable<Book> getAll() {
        return books.findAll();
    }

    @Operation(summary = "Add a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        try {
            Book savedBook = books.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Object> updateBook(@RequestBody Book book) {
        if (books.findById(book.getId()).isPresent()) {
            Book updatedBook = books.save(book);
            return ResponseEntity.ok(updatedBook);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Book not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteBook(@RequestParam long id) {
        if (books.findById(id).isPresent()) {
            books.deleteById(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Book deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Book not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Add a new book with author and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/addNewBookWithAuthorAndCategory")
    public ResponseEntity<Object> addNewBookWithAuthorAndCategory(@RequestBody NewBook newBook) {
        try {
            Author author = new Author();
            author.setFirstName(newBook.getAuthorFirstName());
            author.setLastName(newBook.getAuthorLastName());
            author.setBiography(newBook.getAuthorBiography());
            Author newAuthor = authors.save(author);

            Set<Author> newAuthors = new HashSet<>();
            newAuthors.add(newAuthor);

            Category category = new Category();
            category.setName(newBook.getCategoryName());
            Category newCategory = categories.save(category);

            Book book = new Book();
            book.setAuthors(newAuthors);
            book.setCategory(newCategory);
            book.setTitle(newBook.getBookTitle());

            Book savedBook = books.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
