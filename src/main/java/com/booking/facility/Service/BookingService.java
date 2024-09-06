package com.booking.facility.Service;

import com.booking.facility.Model.Facility;
import com.booking.facility.Model.Timeslot;
import com.booking.facility.Model.TimeslotDTO;
import com.booking.facility.Repository.FacilityRepository;
import com.booking.facility.Repository.TimeslotRepository;
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

            return timeslotRepository.findByFacilityNameAndDate(facilityName,date);

    }


    public Timeslot bookTimeslot(Long slotid,String username){
        Timeslot check = timeslotRepository.findTimeslotById(slotid);

        if(check.isBooked()){
            System.out.println("already booked");
            return check;
        }else{
            check.setBooked(true);
            check.setUsername(username);
            check = timeslotRepository.save(check);
            return check;
        }
    }

    public Timeslot unbookTimeslot(Long slotid,String username){
        Timeslot check = timeslotRepository.findTimeslotById(slotid);

        if(check.isBooked()){
            check.setBooked(false);
            check.setUsername("");
            check = timeslotRepository.save(check);
            return check;
        }else{
            System.out.println("Not yet booked");
            return check;
        }

    }


    public List<Timeslot> showUserTimeslots(String username) {
        List<Timeslot> usertimeslot = timeslotRepository.findByUsername(username);

        return usertimeslot;
    }

    public List<Timeslot> getAllTimeslotbyName(String facilityname) {
        return timeslotRepository.findByFacilityName(facilityname);
    }

    public List<?> toTimeslotDTO(List<Timeslot> timeslots) {
        List<TimeslotDTO> timeslotdata = new ArrayList<>();
        for (Timeslot var : timeslots)
        {
            TimeslotDTO temp = new TimeslotDTO(var.getId(),var.getStartime(),var.getEndtime(),
                    var.getDate(),var.getDuration(),var.isBooked(), var.getUsername(), var.getFacility().getName());
            timeslotdata.add(temp);

        }

        return timeslotdata;
    }

    public boolean checkIsbooked(Long id) {
        return timeslotRepository.findTimeslotById(id) != null && timeslotRepository.findTimeslotById(id).isBooked();
    }

    public boolean checkIsNotbooked(Long id) {
        return timeslotRepository.findTimeslotById(id) != null && !timeslotRepository.findTimeslotById(id).isBooked();
    }

    public Timeslot getTimeslotbyId(Long id) {
        return timeslotRepository.findTimeslotById(id);
    }
}
