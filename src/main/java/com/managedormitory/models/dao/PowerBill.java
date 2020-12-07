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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id", length = 20)
    private Integer billId;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(length = 20)
    private Long numberOfPowerBegin;

    @Column(length = 20)
    private Long numberOfPowerEnd;

    @Column(length = 20)
    private Long numberOfPowerUsed;

    @Column(length = 20)
    private Float numberOfMoneyMustPay;

    @Column
    private boolean isPay;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    @NonNull
    //@NotBlank
    private Room room;

    @OneToOne
    @JoinColumn(name = "price_list_id")
    @NonNull
    private PriceList priceList;

    /*@Embeddable
    public static class Id implements Serializable {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    protected Id id;*/
}