package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Integer month;

    @Column
    private Integer year;

    @Column
    @NonNull
    @NotBlank(message = "Start Date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @NonNull
    @NotBlank
    protected Student student;

    @Column
    private boolean isPay;
}