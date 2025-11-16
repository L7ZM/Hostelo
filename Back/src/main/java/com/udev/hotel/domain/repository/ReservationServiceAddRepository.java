package com.udev.hotel.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.domain.entity.ReservationServiceAdd;

public interface ReservationServiceAddRepository extends JpaRepository<ReservationServiceAdd, Long> {
    List<ReservationServiceAdd> findByReservation(Reservation reservation);
}
