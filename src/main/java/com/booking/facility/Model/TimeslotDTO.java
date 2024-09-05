package com.booking.facility.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeslotDTO {

    private Long id;
    private LocalTime startime;
    private LocalTime endtime;
    private LocalDate date;
    private int duration;
    private boolean booked;
    private String username;
    private String facilityname;
}
