package com.managedormitory.message.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.managedormitory.models.dao.Campus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    @Size(max = 200)
    private String address;

    @Size(min = 10, max = 11)
    private String phone;

    private List<Campus> campuses;

    private Set role;
}
