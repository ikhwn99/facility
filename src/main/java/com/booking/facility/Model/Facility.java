package com.booking.facility.Model;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "facility")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Facility {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;
    private int slotduration;
    private LocalTime starttime;
    private LocalTime endtime;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Timeslot> slot;

}
