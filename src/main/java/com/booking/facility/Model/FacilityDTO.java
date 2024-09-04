package com.booking.facility.Model;


import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FacilityDTO {

    private String name;
    private String description;
    private int slotduration;
    private LocalTime start;
    private LocalTime end;
}
