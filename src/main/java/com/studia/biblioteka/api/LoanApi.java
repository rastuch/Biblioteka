package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Loan;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.LoanManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
public class LoanApi {
    private LoanManager loans;

    @Autowired
    public LoanApi(LoanManager loans) {
        this.loans = loans;
    }

    @Operation(summary = "Get loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of loan",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public Optional<Loan> getById(@RequestParam long id) {
        return loans.findById(id);
    }

    @Operation(summary = "Get all loans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of all loans",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping("/all")
    public Iterable<Loan> getAll() {
        return loans.findAll();
    }

    @Operation(summary = "Add a new loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Loan addLoan(@RequestBody Loan loan) {
        return loans.save(loan);
    }

    @Operation(summary = "Update an existing loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public Loan updateLoan(@RequestBody Loan loan) {
        return loans.save(loan);
    }

    @Operation(summary = "Delete a loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<?> deleteLoan(@RequestParam long id) {
        try {
            loans.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Loan deleted successfully");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
