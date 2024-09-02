package com.booking.facility.Repository;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility,Long> {

//    @Query("SELECT f FROM Facility f JOIN f.slot t WHERE f.name = :facilityName AND t.date = :date")
//    Facility findByFacilityNameAndDate(@Param("facilityName") String facilityName, @Param("date") LocalDate date);

    Facility findByName(String facilityName);

}
