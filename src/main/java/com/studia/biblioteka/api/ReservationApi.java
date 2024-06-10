package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.Reservation;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewReservationByCopyId;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.CopyManager;
import com.studia.biblioteka.manager.ReservationManager;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservation")
public class ReservationApi {
    private ReservationManager reservations;
    private CopyManager copies;
    private UserManager users;

    @Autowired
    public ReservationApi(ReservationManager reservations, CopyManager copies, UserManager users) {
        this.reservations = reservations;
        this.copies = copies;
        this.users = users;
    }

    @Operation(summary = "Get reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reservation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getById(@RequestParam long id) {
        return reservations.findById(id)
                .map(reservation -> ResponseEntity.ok((Object) reservation))
                .orElseGet(() -> {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Reservation not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @Operation(summary = "Get all reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all reservations",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation[].class)))
    })
    @GetMapping("/all")
    public Iterable<Reservation> getAll() {
        return reservations.findAll();
    }

    @Operation(summary = "Get all reservations by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all reservations for this user",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Reservation.class))))
    })
    @GetMapping("/allByUserId")
    public Iterable<Reservation> getAllByUserId(@RequestParam long id) {
        return reservations.findAllByUserId(id);
    }

    @Operation(summary = "Add a new reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> addReservation(@RequestBody Reservation reservation) {
        try {
            Reservation savedReservation = reservations.save(reservation);
            return ResponseEntity.ok(savedReservation);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Add a new reservation by copy id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or copy not found or is not AVAILABLE",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/addByCopyId")
    public ResponseEntity<Object> addReservation(@RequestBody NewReservationByCopyId newReservation) {
        try {
            var copyToReservation = copies.findById(newReservation.getCopyId());
            if(copyToReservation.isPresent() && copyToReservation.get().getStatus() == CopyStatus.AVAILABLE) {
                var copyToSave = copyToReservation.get();
                copyToSave.setStatus(CopyStatus.RESERVED);

                var user = users.findById(newReservation.getUserId());
                if(user.isPresent()) {
                    var reservationToSave = Reservation.builder()
                            .reservationDate(LocalDate.now())
                            .copy(copyToSave)
                            .user(user.get())
                            .status("")
                            .build();
                    reservations.save(reservationToSave);
                    return ResponseEntity.ok(reservationToSave);
                } else {
                   return ResponseEntity.status(404).body("User not found");
                }
            } else {
                return ResponseEntity.status(404).body("Copy not found or is not available");
            }
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update an existing reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<Object> updateReservation(@RequestBody Reservation reservation) {
        if (reservations.findById(reservation.getId()).isPresent()) {
            Reservation updatedReservation = reservations.save(reservation);
            return ResponseEntity.ok(updatedReservation);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Reservation not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteReservation(@RequestParam long id) {
        var currentReservation = reservations.findById(id);
        if (currentReservation.isPresent()) {
            var reservedCopy = copies.findById(currentReservation.get().getCopy().getId());
            if(reservedCopy.isPresent() && reservedCopy.get().getStatus() == CopyStatus.RESERVED){
                var copyToSave = reservedCopy.get();
                copyToSave.setStatus(CopyStatus.AVAILABLE);
                copies.save(copyToSave);
            }
            reservations.delete(id);
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setMessage("Reservation deleted successfully");
            return ResponseEntity.ok(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Reservation not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
