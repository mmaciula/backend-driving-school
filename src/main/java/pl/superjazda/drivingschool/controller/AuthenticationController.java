package pl.superjazda.drivingschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.model.ResponseMessage;
import pl.superjazda.drivingschool.model.Role;
import pl.superjazda.drivingschool.model.RoleType;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.LoginDto;
import pl.superjazda.drivingschool.model.dto.RegisterDto;
import pl.superjazda.drivingschool.model.dto.TokenDto;
import pl.superjazda.drivingschool.repository.RoleRepository;
import pl.superjazda.drivingschool.repository.UserRepository;
import pl.superjazda.drivingschool.security.JwtTokenUtil;
import pl.superjazda.drivingschool.security.JwtUserDetails;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil tokenUtil;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto logIn) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(logIn.getUsername(), logIn.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenUtil.generateToken(auth);

        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new TokenDto(jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getSurname(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto register) {
        if (userRepository.existsByUsername(register.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Username already exists!"));
        }

        if (userRepository.existsByEmail(register.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Email is already in use!"));
        }

        User user = new User(register.getUsername(), register.getEmail(), passwordEncoder.encode(register.getPassword()),
                register.getName(), register.getSurname());

        Set<String> strRoles = register.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Role not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFoundException("Role not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleType.ROLE_MODERATOR)
                                .orElseThrow(() -> new RoleNotFoundException("Role not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException("Role not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }
}
