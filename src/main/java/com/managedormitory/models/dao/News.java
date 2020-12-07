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
    @Column(length = 20)
    private Integer id;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String introduction;

    @Column(length = 255)
    private String content;

    @Column(length = 255)
    private String pathImage;

    @Column(length = 255)
    private String informationSource;

    @Column
    private Date createDate;
}
