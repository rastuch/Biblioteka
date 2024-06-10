package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Fine;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewFineByUserId;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.FineManager;
import com.studia.biblioteka.manager.UserManager;
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
@RequestMapping("/api/fine")
public class FineApi {
    private FineManager fines;
    private UserManager users;

    @Autowired
    public FineApi(FineManager fines, UserManager users) {
        this.fines = fines;
        this.users = users;
    }

    @Operation(summary = "Get fine by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the fine",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "404", description = "Fine not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getById(@RequestParam long id) {
        return fines.findById(id)
                .map(fine -> ResponseEntity.ok((Object) fine))
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Fine not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @Operation(summary = "Get all fines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all fines",
                    content = @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = Fine.class))))
    })
    @GetMapping("/all")
    public Iterable<Fine> getAll() {
        return fines.findAll();
    }

    @Operation(summary = "Get all fines by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all fines for the user",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Fine.class))))
    })
    @GetMapping("/allByUserId")
    public Iterable<Fine> getAllByUserId(@RequestParam long id) {
        return fines.findAllByUserId(id);
    }

    @Operation(summary = "Add a new fine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addFine(@RequestBody Fine fine) {
        try {
            Fine savedFine = fines.save(fine);
            return ResponseEntity.ok(savedFine);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Add a new fine by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/addByUserId")
    public ResponseEntity<Object> addFineByUserId(@RequestBody NewFineByUserId fine) {
        try {
            var user = users.findById(fine.getUserId());
            if(user.isPresent()) {
                var fineToSave = Fine.builder()
                        .user(user.get())
                        .reason(fine.getReason())
                        .amount(fine.getAmount())
                        .build();
                var savedFine = fines.save(fineToSave);
                return ResponseEntity.ok(savedFine);
            }else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update an existing fine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Fine not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Object> updateFine(@RequestBody Fine fine) {
        if (fines.findById(fine.getId()).isPresent()) {
            Fine updatedFine = fines.save(fine);
            return ResponseEntity.ok(updatedFine);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Fine not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a fine by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Fine not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteFine(@RequestParam long id) {
        if (fines.findById(id).isPresent()) {
            fines.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Fine deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Fine not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
