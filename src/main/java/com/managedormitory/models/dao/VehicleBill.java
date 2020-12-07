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
public class VehicleBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id", length = 20)
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

    @Column(length = 20)
    @NonNull
    private float payedMoney;

    @ManyToOne
    @MapsId("vehicleId")
    @JoinColumn(name = "vehicle_id")
    ////@NotBlank
    private Vehicle vehicle;

    /*@Embeddable
    public static class Id implements Serializable {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "bill_id")
        protected Integer billId;

        @Column(name = "vehicle_id")
        protected Integer vehicleId;

        @Column
        @JsonFormat(pattern = "yyyy-MM-dd")
        protected LocalDate startDate;

        @Column
        @JsonFormat(pattern = "yyyy-MM-dd")
        protected LocalDate endDate;

        public Id() {

        }

        public Id(Integer billId, Integer vehicleId, LocalDate startDate, LocalDate endDate) {
            this.billId = billId;
            this.vehicleId = vehicleId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.billId.equals(that.billId) && this.vehicleId.equals(that.vehicleId) && this.startDate.equals(that.startDate) && this.endDate.equals(that.endDate);
            }
            return false;
        }
    }

    @EmbeddedId
    protected Id id;*/
}