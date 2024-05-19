package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.CopyManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/copy")
public class CopyApi {
    private CopyManager copies;

    @Autowired
    public CopyApi(CopyManager copies) {
        this.copies = copies;
    }

    @Operation(summary = "Get copy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the copy",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Copy.class))),
            @ApiResponse(responseCode = "404", description = "Copy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getById(@RequestParam long id) {
        return copies.findById(id)
                .map(copy -> ResponseEntity.ok((Object) copy))
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Copy not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @Operation(summary = "Get all copies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all copies",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Copy[].class)))
    })
    @GetMapping("/all")
    public Iterable<Copy> getAll() {
        return copies.findAll();
    }

    @Operation(summary = "Add a new copy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Copy added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Copy.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addCopy(@RequestBody Copy copy) {
        try {
            Copy savedCopy = copies.save(copy);
            return ResponseEntity.ok(savedCopy);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update an existing copy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Copy updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Copy.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Copy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Object> updateCopy(@RequestBody Copy copy) {
        if (copies.findById(copy.getId()).isPresent()) {
            Copy updatedCopy = copies.save(copy);
            return ResponseEntity.ok(updatedCopy);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Copy not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a copy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Copy deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Copy not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteCopy(@RequestParam long id) {
        if (copies.findById(id).isPresent()) {
            copies.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Copy deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Copy not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
