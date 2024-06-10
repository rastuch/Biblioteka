package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Loan;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dao.enums.LoanStatus;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewLoan;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.CopyManager;
import com.studia.biblioteka.manager.LoanManager;
import com.studia.biblioteka.manager.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
public class LoanApi {
    private LoanManager loans;
    private CopyManager copies;
    private UserManager users;

    @Autowired
    public LoanApi(LoanManager loans, CopyManager copies, UserManager users) {
        this.copies = copies;
        this.users = users;
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addLoan(@RequestBody NewLoan loan) {
        try {
            var copy = copies.findById(loan.copyId);
            var user = users.findById(loan.userId);
            var now = LocalDate.now();
            //Add max date to return book
            var maxDate = now.plusMonths(1);

            if (copy.isPresent() && user.isPresent()) {

                //Check is already borrowed
                if(copy.get().getStatus() == CopyStatus.BORROWED){
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Book is already borrowed");
                    return ResponseEntity.badRequest().body(errorResponse);
                }

                //Save new copy borrowed status
                var copyToChange = copy.get();
                copyToChange.setStatus(CopyStatus.BORROWED);
                var copyToSave = copies.save(copyToChange);

                var newLoan = Loan.builder()
                        .user(user.get())
                        .copy(copyToSave)
                        .loanDate(LocalDate.now())
                        .maxReturnDate(maxDate)
                        .status(LoanStatus.IN_PROGRESS)
                        .build();
                loans.save(newLoan);
               return ResponseEntity.ok().body("New Loan Added successfully");
            } else {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage("User or book not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
    } catch (RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
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

    @Operation(summary = "All loan statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All statuses",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LoanStatus.class))))
    })
    @GetMapping("/statuses")
    public ResponseEntity<LoanStatus[]> getCopyStatuses() {
        return ResponseEntity.ok(LoanStatus.values());
    }
}
