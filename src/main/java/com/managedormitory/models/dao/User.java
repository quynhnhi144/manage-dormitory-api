package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer userId;

    @Column(length = 100, unique = true, nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    //@NotBlank(message = "Username is mandatory")
    private String username;

    @Column(nullable = false)
    @NonNull
    //@NotBlank(message = "Password is mandatory")
    private String password;

    @Column(length = 100, name = "full_name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    //@NotBlank(message = "Full Name is mandatory")
    private String fullName;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Email
    @NonNull
    //@NotBlank(message = "Email is mandatory")
    private String email;

    @Column(length = 100)
    private String address;

    @Column(length = 11, nullable = true)
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userManager")
    @JsonIgnore
    private List<Campus> campuses;
}
