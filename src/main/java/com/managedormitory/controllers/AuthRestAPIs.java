package com.managedormitory.controllers;

import com.managedormitory.message.request.LoginForm;
import com.managedormitory.message.request.SignUpForm;
import com.managedormitory.message.response.JwtResponse;
import com.managedormitory.message.response.ResponseMessage;
import com.managedormitory.models.dao.Role;
import com.managedormitory.models.dao.RoleName;
import com.managedormitory.models.dao.User;
import com.managedormitory.repositories.RoleRepository;
import com.managedormitory.repositories.UserRepository;
import com.managedormitory.security.jwt.JwtProvider;
import com.managedormitory.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(), jwtExpiration));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ResponseMessage("Thất bại -> Username đã tồn tại!!"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ResponseMessage("Thất bại -> Email đã tồn tại!!!"), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                DateUtil.getSDateFromLDate(DateUtil.getLDateFromString(signUpRequest.getBirthday())),
                signUpRequest.getEmail(),
                signUpRequest.getAddress(),
                signUpRequest.getPhone());

        Set strRoles = signUpRequest.getRole();
        Set roles = new HashSet();
        strRoles.forEach(role -> {
            switch (role.toString()) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Thất bại -> Bởi vì: User Role không được tìm thấy!!!"));
                    roles.add(adminRole);
                    break;

                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Thất bại -> Bởi vì: User Role không được tìm thấy!!!"));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity(new ResponseMessage("User đã đăng ký thành công!!!"), HttpStatus.OK);
    }
}
