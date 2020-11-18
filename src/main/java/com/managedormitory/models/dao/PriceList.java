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
    @Column
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    //@NotBlank(message = "Name is mandatory")
    private String name;

    @Column
    @NonNull
    //@NotBlank
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
