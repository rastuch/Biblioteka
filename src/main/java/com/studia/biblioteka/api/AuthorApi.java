package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Author;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.AuthorManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/author")
public class AuthorApi {
    private AuthorManager authors;

    @Autowired
    public AuthorApi(AuthorManager authors) {
        this.authors = authors;
    }

    @Operation(summary = "Get author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the author",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getById(@RequestParam long id) {
        return authors.findById(id)
                .map(author -> ResponseEntity.ok((Object) author))
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Author not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @Operation(summary = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all authors",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author[].class)))
    })
    @GetMapping("/all")
    public Iterable<Author> getAll() {
        return authors.findAll();
    }

    @Operation(summary = "Add a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addAuthor(@RequestBody Author author) {
        try {
            Author savedAuthor = authors.save(author);
            return ResponseEntity.ok(savedAuthor);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update an existing author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Object> updateAuthor(@RequestBody Author author) {
        if (authors.findById(author.getId()).isPresent()) {
            Author updatedAuthor = authors.save(author);
            return ResponseEntity.ok(updatedAuthor);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Author not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteAuthor(@RequestParam long id) {
        if (authors.findById(id).isPresent()) {
            authors.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Author deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Author not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
