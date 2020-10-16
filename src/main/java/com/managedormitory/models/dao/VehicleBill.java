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

    @Embeddable
    public static class Id implements Serializable {
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
    protected Id id;

    @Column
    private boolean isPay;

    @ManyToOne
    @MapsId("vehicleId")
    @JoinColumn(name = "vehicle_id")
    @NonNull
    @NotBlank
    protected Vehicle vehicleId;

    @OneToOne
    @JoinColumn(name = "price_list_id")
    @NonNull
    @NotBlank
    protected PriceList priceListId;
}