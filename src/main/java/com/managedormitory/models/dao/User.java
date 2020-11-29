package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.managedormitory.models.dto.CampusDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

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
    private Date birthday;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, String fullName, Date birthday, String email, String address, String phone) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

}
