package com.booking.facility.Service;

import com.booking.facility.Model.Facility;
import com.booking.facility.Repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    @Autowired
    FacilityRepository facilityRepository;


    public Facility getFacility(String name){
        return facilityRepository.findByName(name);
    }
}
