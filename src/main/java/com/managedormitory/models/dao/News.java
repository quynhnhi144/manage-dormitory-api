package com.managedormitory.models.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String introduction;

    @Column
    private String content;

    @Column
    private String informationSoruce;

    @Column
    private Date createDate;
}
