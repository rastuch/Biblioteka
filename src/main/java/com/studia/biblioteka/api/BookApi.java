package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.BookRepo;
import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.AddNewBook;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.BookManager;
import com.studia.biblioteka.manager.CopyManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookApi {
    private BookManager bookManager;
    private CopyManager copies;

    @Autowired
    public BookApi(BookManager bookManager, CopyManager copies) {
        this.bookManager = bookManager;
        this.copies = copies;
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
        return bookManager.findById(id)
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
                    content = @Content(mediaType = "application/json",  array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    })
    @GetMapping("/all")
    public Iterable<Book> getAll(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return bookManager.findByKeyword(search);
        } else {
            return bookManager.findAll();
        }
    }

    @Operation(summary = "Add a new book and copies with AVAILABLE status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody AddNewBook book) {
        try {
            Book newBook = new Book();
            newBook.setTitle(book.title);
            newBook.setCategory(book.category);
            newBook.setAuthors(book.authors);
            Book savedBook = bookManager.save(newBook);
            for(int i = 0; i <= book.countOfCopy; i++){
                Copy copy = Copy.builder()
                        .book(savedBook)
                        .location(book.copyLocation)
                        .status(CopyStatus.AVAILABLE)
                        .build();
                copies.save(copy);
            }
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
        if (bookManager.findById(book.getId()).isPresent()) {
            Book updatedBook = bookManager.save(book);
            return ResponseEntity.ok(updatedBook);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Book not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a book and copies by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book and copies are deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteBook(@RequestParam long id) {
        copies.deleteAllBookCopies(id);
        if (bookManager.findById(id).isPresent()) {
            bookManager.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Book and copies are deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Book not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
