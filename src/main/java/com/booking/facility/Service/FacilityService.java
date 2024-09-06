package com.booking.facility.Service;

import com.booking.facility.Model.Facility;
import com.booking.facility.Repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacilityService {

    @Autowired
    FacilityRepository facilityRepository;


    public Facility getFacility(String name){
        return facilityRepository.findByName(name);
    }

    public List<Facility> getAllFacility() {
        return facilityRepository.findAll();
    }
}
