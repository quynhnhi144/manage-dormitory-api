package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    private String name;

    @Column(length = 20)
    @NonNull
    private Float price;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "priceList")
    @JsonIgnore
    private PowerBill powerBill;

    public PriceList(PowerBillDto powerBillDto){
        this.id = powerBillDto.getIdPriceList();
        this.name = powerBillDto.getNamePriceList();
        this.price = powerBillDto.getPriceAKWH();
    }

}
