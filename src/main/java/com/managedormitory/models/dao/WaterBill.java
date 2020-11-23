package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WaterBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @Column
    @NonNull
    private float payedMoney;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    /*@Embeddable
    public static class Id implements Serializable {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "bill_id")
        protected Integer billId;

        @Column(name = "student_id")
        protected Integer studentId;

        @Column
        protected LocalDate startDate;

        @Column
        protected LocalDate endDate;

        public Id() {

        }

        public Id(Integer billId, Integer studentId, LocalDate startDate, LocalDate endDate) {
            this.billId = billId;
            this.studentId = studentId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.billId.equals(that.billId) && this.studentId.equals(that.studentId) && this.startDate.equals(that.startDate) && this.endDate.equals(that.endDate);
            }
            return false;
        }
    }

    @EmbeddedId
    protected Id id;*/
}