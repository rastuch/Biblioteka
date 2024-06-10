package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepo extends CrudRepository<Reservation, Long> {
    Iterable<Reservation> findAllByUserId(Long userId);
}
