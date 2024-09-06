package com.booking.facility.Controller;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Service.BookingService;
import com.booking.facility.Service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

        StringBuilder errormessage = new StringBuilder("Error message\n");
        boolean error = false;


        List<Timeslot> timeslots = new ArrayList<>();
        if (facilityService.getFacility(facilityname) == null){
            errormessage.append("Facility not available\n");
            error = true;
        }
        if(date!=null && date.isBefore(LocalDate.now())){
            errormessage.append("Cannot show past timeslot\n");
            error = true;
        }

        if(!error && date==null) {
            timeslots = bookingService.getAllTimeslotbyName(facilityname);
        }
        if(!error && date!=null){
            timeslots = bookingService.getTimeslot(facilityname, date);
        }

        if(!error){
            return ResponseEntity.ok(bookingService.toTimeslotDTO(timeslots));
        }else{
            return ResponseEntity.status(404).body(errormessage);
        }


    }

    @GetMapping("/facility")
    public ResponseEntity <?> fetchFacility(@RequestParam(required = false) String facilityname){

        try {
            if(facilityname==null)
                return ResponseEntity.ok(facilityService.getAllFacility());
            else {
                Facility facility=  facilityService.getFacility(facilityname);
                if(facility==null)
                    return ResponseEntity.status(404).body("facility not available");
                else
                    return ResponseEntity.ok(facility);
            }

        }catch(Exception e){
            System.out.println("facility not available");
            return ResponseEntity.status(404).body("facility not available");
        }
    }

    @GetMapping("/admin/facility")
    public ResponseEntity<?> generateFacility(  @RequestParam String name,
                                                @RequestParam Optional<String> description,
                                                @RequestParam int slotduration,
                                                @RequestParam LocalTime start,
                                                @RequestParam LocalTime end){

        System.out.println("start:"+start);
        System.out.println("Min starttime:"+MIN_STARTTIME);
        System.out.println(start.isBefore(MIN_STARTTIME) || end.isAfter(MAX_ENDTIME));
        String desc = description.orElse("");
        Facility check = facilityService.getFacility(name);
        System.out.println(check);
        StringBuilder errormessage = new StringBuilder("Error Message\n");
        boolean error = false;
        if (check != null) {
            errormessage.append("facility name already taken\n");
            error = true;
        }
        if (slotduration > MAX_TIMESLOT_DURATION || slotduration < MIN_TIMESLOT_DURATION) {
            errormessage.append("slot duration shorter than " + MIN_TIMESLOT_DURATION +" or longer than "+ MAX_TIMESLOT_DURATION+"\n");
            error = true;
        }
        if (start.isBefore(MIN_STARTTIME) || end.isAfter(MAX_ENDTIME)) {
            errormessage.append("start time is before "+ MIN_STARTTIME + " or end time after " + MAX_ENDTIME+"\n");
            error = true;
        }
        if (!error){
            Facility facility = bookingService.generateFacility(name, desc, slotduration, start, end);
            return ResponseEntity.ok(facility);
        }else{
            return ResponseEntity.accepted().body(errormessage);
        }

    }


    @GetMapping("/admin/timeslot")
    public ResponseEntity<?> generateTimeslot(@RequestParam String facilityname,
                                              @RequestParam LocalDate date){

        List<Timeslot> timeslots = bookingService.getTimeslot(facilityname,date);
        Facility check = facilityService.getFacility(facilityname);

        StringBuilder errormessage = new StringBuilder("Error message\n");
        boolean error = false;

        System.out.println(timeslots);
        if(check==null){
            errormessage.append("facility not available\n");
            error=true;
        }
        if (!timeslots.isEmpty()){
            errormessage.append("timeslot already generated\n");
            error=true;
        }
        if(date.isBefore(LocalDate.now())){
            errormessage.append("cannot generate timeslot for past date\n");
            error=true;
        }
        if(!error){
            System.out.println("timeslots generated");
            timeslots = bookingService.generateTimeslot(facilityname,date);
            return ResponseEntity.ok(timeslots);
        }else{
            return ResponseEntity.accepted().body(errormessage);
        }
    }

    @GetMapping("/booking")
    public ResponseEntity<?> bookTimeslot(@RequestParam String action,
                                          @RequestParam Long timeslotid,
                                          @RequestParam Optional<String> username) {

        String name = username.orElse("guest");
        Timeslot time = null;

        if(action.equalsIgnoreCase("book")){
            if(bookingService.checkIsNotbooked(timeslotid)){
                time = bookingService.bookTimeslot(timeslotid,name);
            }else {
                return ResponseEntity.status(404).body("already booked");
            }
        }
        else if (action.equalsIgnoreCase("unbook"))
            if(bookingService.checkIsbooked(timeslotid)) {
                time = bookingService.unbookTimeslot(timeslotid, name);
            }else {
                return ResponseEntity.status(404).body("cannot unbook timeslot thats not yet booked");
            }

        return ResponseEntity.ok(time);

    }

    @GetMapping("/user")
    public ResponseEntity<?> userPage(@RequestParam String username) {

        List<Timeslot> usertimeslots = bookingService.showUserTimeslots(username);

        return ResponseEntity.ok(usertimeslots);
    }
}
