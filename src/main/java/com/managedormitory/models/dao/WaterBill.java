package com.managedormitory.models.dao;

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

    @Embeddable
    public static class Id implements Serializable {
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
    protected Id id;

    @Column
    private boolean isPay;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @NonNull
    protected Student studentId;

    @OneToOne
    @JoinColumn(name = "price_list_is")
    @NonNull
    private PriceList priceListId;
}