package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailRoom {

    @EmbeddedId
    protected Id id;

    @Column
    private LocalDate month;

    @Column
    private LocalDate year;

    @Column
    private boolean isPay;

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

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    @NonNull
    @NotBlank
    protected Room room;

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "student_id")
        protected Integer studentId;

        @Column(name = "room_id")
        protected Integer roomId;

        public Id() {

        }

        public Id(Integer studentId, Integer roomId) {
            this.studentId = studentId;
            this.roomId = roomId;
        }

        public boolean equals(Object o) {
            if (o instanceof Id) {
                Id that = (Id) o;
                return this.studentId.equals(that.studentId) && this.roomId.equals(that.roomId);
            }
            return false;
        }
    }
}