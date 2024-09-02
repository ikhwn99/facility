package com.booking.facility.Controller;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Service.BookingService;
import com.booking.facility.Service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

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
        if(check==null && checkdatepast && checkdatelimit){
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

    @GetMapping("/facility/{name}/{slotduration}/{start}/{end}")
    public ResponseEntity <Facility> generateFacility(@PathVariable String name, @PathVariable int slotduration,@PathVariable LocalTime start,@PathVariable LocalTime end){

        Facility check = facilityService.getFacility(name);

        if(check!=null) {
            System.out.println("facility name already taken");
            return null;
        } else if (slotduration > MAX_TIMESLOT_DURATION || slotduration < MIN_TIMESLOT_DURATION) {
            System.out.println("slot duration too short or too long");
            return null;
        } else if (start.isBefore(MIN_STARTTIME) && end.isAfter(MAX_ENDTIME)){
            System.out.println("start time is before working hour or end time after midninght ");
            return null;
        } else {
            Facility facility = bookingService.generateFacility(name, slotduration, start, end);

            return ResponseEntity.ok(facility);
        }
    }
}
