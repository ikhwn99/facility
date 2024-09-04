package com.booking.facility.Controller;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.FacilityDTO;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Service.BookingService;
import com.booking.facility.Service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FacilityController {

    public static final int MAX_DAY_FUTURE_BOOKING = 7;
    public static final int MIN_TIMESLOT_DURATION = 30;
    public static final int MAX_TIMESLOT_DURATION = 120;
    public static final LocalTime MIN_STARTTIME = LocalTime.of(5, 0);
    public static final LocalTime MAX_ENDTIME = LocalTime.of(23,0);;

    @Autowired
    BookingService bookingService;

    @Autowired
    FacilityService facilityService;

    @GetMapping("/timeslots/{facilityname}/{date}/{generate}")
    public ResponseEntity<List<Timeslot>> getTimeslots(@PathVariable String facilityname, @PathVariable LocalDate date, @PathVariable boolean generate) {
        List<Timeslot> timeslots = bookingService.getTimeslot(facilityname,date);

        Facility check = facilityService.getFacility(facilityname);
        boolean checkdatepast = date.isBefore(LocalDate.now());
        boolean checkdatelimit = date.isAfter(LocalDate.now().plusDays(MAX_DAY_FUTURE_BOOKING));
        if(check==null && (checkdatepast || checkdatelimit)){
            System.out.println("facility not available or date past/more than a week from today");
            return null;
        }
        if(timeslots.isEmpty() && generate){
            System.out.println("entered generate");
            return ResponseEntity.ok(bookingService.generateTimeslot(facilityname,date));
        }else if (!timeslots.isEmpty()){
            System.out.println("shown timeslots");
            return ResponseEntity.ok(bookingService.getTimeslot(facilityname,date));
        }else {
            System.out.println("time slot not generated yet");
            return null;
        }
    }

    @GetMapping("/facility/{facilityname}")
    public ResponseEntity <?> fetchFacility(@PathVariable(required = false) String facilityname){

        try {
            if(facilityname.isEmpty())
                return ResponseEntity.ok(facilityService.getAllFacility());
            else
                return ResponseEntity.ok(facilityService.getFacility(facilityname));

        }catch(Exception e){
            System.out.println("facility not available");
            return null;
        }
    }

    @GetMapping("/admin/facility")
    public ResponseEntity<Facility> generateFacility(@RequestBody FacilityDTO input){

        Facility check = facilityService.getFacility(input.getName());

        if(check!=null) {
            System.out.println("facility name already taken");
            return null;
        } else if (input.getSlotduration() > MAX_TIMESLOT_DURATION || input.getSlotduration() < MIN_TIMESLOT_DURATION) {
            System.out.println("slot duration too short or too long");
            return null;
        } else if (input.getStart().isBefore(MIN_STARTTIME) && input.getEnd().isAfter(MAX_ENDTIME)){
            System.out.println("start time is before working hour or end time after midninght ");
            return null;
        } else {
            Facility facility = bookingService.generateFacility(input.getName(), input.getDescription(), input.getSlotduration(), input.getStart(), input.getEnd());

            return ResponseEntity.ok(facility);
        }
    }



}
