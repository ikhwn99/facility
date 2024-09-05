package com.booking.facility.Controller;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Service.BookingService;
import com.booking.facility.Service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    public static final int MIN_TIMESLOT_DURATION = 30;
    public static final int MAX_TIMESLOT_DURATION = 120;
    public static final LocalTime MIN_STARTTIME = LocalTime.of(5, 0);
    public static final LocalTime MAX_ENDTIME = LocalTime.of(23,0);

    @Autowired
    BookingService bookingService;

    @Autowired
    FacilityService facilityService;

    @GetMapping("/timeslots")
    public ResponseEntity<?> getTimeslots(@RequestParam String facilityname,@RequestParam (required = false) LocalDate date) {

        List<Timeslot> timeslots;

        if(date==null) {
            timeslots = bookingService.getAllTimeslotbyName(facilityname);
            if(timeslots==null)
                return ResponseEntity.notFound().build();
        }else {
            timeslots = bookingService.getTimeslot(facilityname, date);
            boolean checkdatepast = date.isBefore(LocalDate.now());
            if (timeslots == null && checkdatepast) {
                System.out.println("facility not available or past");
                return null;
            }
        }
        return ResponseEntity.ok(bookingService.toTimeslotDTO(timeslots));


    }

    @GetMapping("/facility")
    public ResponseEntity <?> fetchFacility(@RequestParam(required = false) String facilityname){

        try {
            if(facilityname==null)
                return ResponseEntity.ok(facilityService.getAllFacility());
            else
                return ResponseEntity.ok(facilityService.getFacility(facilityname));

        }catch(Exception e){
            System.out.println("facility not available");
            return null;
        }
    }

    @PostMapping("/admin/facility")
    public ResponseEntity<?> generateFacility(  @RequestParam String name,
                                                @RequestParam Optional<String> description,
                                                @RequestParam int slotduration,
                                                @RequestParam LocalTime start,
                                                @RequestParam LocalTime end){

        String desc = description.orElse("");
        Facility check = facilityService.getFacility(name);
        if (check != null) {
            System.out.println("facility name already taken");
            return null;
        } else if (slotduration > MAX_TIMESLOT_DURATION || slotduration< MIN_TIMESLOT_DURATION) {
            System.out.println("slot duration shorter than " + MIN_TIMESLOT_DURATION +" or longer than "+ MAX_TIMESLOT_DURATION);
            return null;
        } else if (start.isBefore(MIN_STARTTIME) && end.isAfter(MAX_ENDTIME)) {
            System.out.println("start time is before "+ MIN_STARTTIME + "or end time after " + MAX_ENDTIME);
            return null;
        } else {
            Facility facility = bookingService.generateFacility(name, desc, slotduration, start, end);
            return ResponseEntity.ok(facility);
        }

    }


    @PostMapping("/admin/timeslot")
    public ResponseEntity<?> generateTimeslot(@RequestParam String facilityname,
                                              @RequestParam LocalDate date){

        List<Timeslot> timeslots;

        timeslots = bookingService.getTimeslot(facilityname,date);

        if (timeslots!=null){
            System.out.println("Already generated");
            return ResponseEntity.status(409).body("Already generated");
        }else {
            System.out.println("timeslots generated");
            timeslots = bookingService.generateTimeslot(facilityname,date);
            return ResponseEntity.ok(timeslots);
        }
    }

    @GetMapping("/booking")
    public ResponseEntity<?> bookTimeslot(@RequestParam String action,
                                          @RequestParam Long timeslotid,
                                          @RequestParam Optional<String> username) {


        String name = username.orElse("guest");
        Timeslot time = null;

        if(action.equalsIgnoreCase("book"))
            time = bookingService.bookTimeslot(timeslotid,name);
        else if (action.equalsIgnoreCase("unbook"))
            time = bookingService.unbookTimeslot(timeslotid,name);


        return ResponseEntity.ok(time);

    }

    @GetMapping("/user")
    public ResponseEntity<?> userPage(@RequestParam String username) {

        List<Timeslot> usertimeslots = bookingService.showUserTimeslots(username);


        return ResponseEntity.ok(usertimeslots);
    }
}
