package com.managedormitory.models.dao;

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
public class PowerBill {

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "bill_id")
        protected Integer billId;

        @Column(name = "room_id")
        protected Integer roomId;

        @Column
        protected LocalDate startDate;

        @Column
        protected LocalDate endDate;

        public Id() {

        }

        public Id(Integer billId, Integer roomId, LocalDate startDate, LocalDate endDate) {
            this.billId = billId;
            this.roomId = roomId;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.billId.equals(that.billId) && this.roomId.equals(that.roomId) && this.startDate.equals(that.startDate) && this.endDate.equals(that.endDate);
            }
            return false;
        }
    }

    @EmbeddedId
    protected Id id;

    @Column
    private Long numberOfPowerBegin;

    @Column
    private Long numberOfPowerEnd;

    @Column
    private Long numberOfPowerUsed;

    @Column
    private boolean isPay;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    @NonNull
    @NotBlank
    protected Room room;
}