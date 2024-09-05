package com.booking.facility.Repository;

import com.booking.facility.Model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    Timeslot findTimeslotById(Long slotid);

    @Query("SELECT t FROM Timeslot t JOIN t.facility f WHERE f.name = :facilityName AND t.date = :date")
    List<Timeslot> findByFacilityNameAndDate(@Param("facilityName") String facilityName, @Param("date") LocalDate date);

    List<Timeslot> findByUsername(String username);

    List<Timeslot> findByFacilityName(String facilityname);
}
