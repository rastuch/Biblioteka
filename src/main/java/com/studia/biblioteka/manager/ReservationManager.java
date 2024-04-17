package com.studia.biblioteka.manager;
import com.studia.biblioteka.dao.ReservationRepo;
import com.studia.biblioteka.dao.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationManager {
    private final ReservationRepo reservationRepo;
    @Autowired
    public ReservationManager(ReservationRepo reservationRepo){
        this.reservationRepo = reservationRepo;
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepo.findById(id);
    }
    public Iterable<Reservation> findAll() {
        return reservationRepo.findAll();
    }

    public Reservation save(Reservation reservation) {
        return reservationRepo.save(reservation);
    }
    public void delete(Long id) {
        reservationRepo.deleteById(id);
    }
}
