package com.booking.facility.Model;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private List<Timeslot> slot;
}
