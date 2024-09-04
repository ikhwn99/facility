package com.booking.facility.Service;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Repository.FacilityRepository;
import com.booking.facility.Repository.TimeslotRepository;
import com.booking.facility.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;


    public Facility generateFacility(String name, String description, int slotduration, LocalTime start, LocalTime end){

        Facility newfacility = new Facility();

        newfacility.setName(name);
        newfacility.setDescription(description);
        newfacility.setStarttime(start);
        newfacility.setEndtime(end);
        newfacility.setSlotduration(slotduration);

        try{
            facilityRepository.save(newfacility);
        }catch (Exception e){
            System.out.println("error during saving new facility , facility detail :" + newfacility);
        }
        return newfacility;
    }

//    public Facility updateFacility(String name,int slotduration,LocalTime start,LocalTime end){
//
//        Facility updatedfacility = new Facility();
//
//        facilityRepository.save(updatedfacility);
//
//        return facilityRepository.findByName(name);
//    }

    public List<Timeslot> generateTimeslot (String facilityName,LocalDate date){
        List<Timeslot> generatedTimeslot = new ArrayList<>();

        Facility facilitydetail = facilityRepository.findByName(facilityName);

        LocalTime starttime = facilitydetail.getStarttime();
        LocalTime endtime = facilitydetail.getEndtime();
        int duration = facilitydetail.getSlotduration();

        try {
            Long totaltime = Duration.between(starttime, endtime).toMinutes();

            while (totaltime > 0 && totaltime >= duration) {
                if (starttime.isBefore(endtime)) {
                    Timeslot temp = new Timeslot();
                    temp.setStartime(starttime);
                    temp.setEndtime(starttime.plusMinutes(duration));
                    temp.setDate(date);
                    temp.setDuration(duration);
                    temp.setFacility(facilitydetail);
                    temp.setBooked(false);
                    temp.setUsername("");

                    generatedTimeslot.add(temp);

                    try {
                        timeslotRepository.save(temp);
                    } catch (DataAccessException dae) {
                        System.out.println("Error saving timeslot for facility name:"+ temp.getFacility().getName()+" and startime: "+temp.getStartime()+" and date:"+temp.getDate());
                    }
                    starttime = starttime.plusMinutes(duration);
                    totaltime -= duration;
                }else{
                    System.out.println("timeslot generated if timeslot < "+ duration+", timeslot:"+totaltime);
                    break;
                }
            }
        } catch (DateTimeException dte) {
            System.out.println("Invalid time calculation for facility ");
        } catch (IllegalArgumentException iae) {
            System.out.println("Invalid arguments for timeslot generation");
        } catch (Exception e) {
            System.out.println("Error generating timeslots");
        }
        return generatedTimeslot;
    }

    
    public List<Timeslot> getTimeslot(String facilityName,LocalDate date) {
//
//                Facility facilitydetail = facilityRepository.findByFacilityNameandDate(facilityName, date);
//
//                if(facilitydetail!=null) {
//                    Long facilityid = facilitydetail.getId();
//                    LocalTime start = facilitydetail.getStarttime();
//                    LocalTime end = facilitydetail.getEndtime();
//                    int duration = facilitydetail.getSlotduration();
//
//                }else{
//                    System.out.println("facility not available");
//                    return null;
//                }
            return timeslotRepository.findByFacilityNameAndDate(facilityName,date);

    }


    public Timeslot bookTimeslot(Long slotid,Long username){
        Timeslot check = timeslotRepository.findTimeslotById(slotid);

        if(check.isBooked()){
            System.out.println("already booked");
            return check;
        }else{
            check.setBooked(true);
        }

        return check;
    }

    public Timeslot unbookTimeslot(Long slotid,Long username){
        Timeslot check = timeslotRepository.findTimeslotById(slotid);

        if(check.isBooked()){
            check.setBooked(false);
            return check;
        }else{
            System.out.println("Not yet booked");
        }
        return check;
    }


}
