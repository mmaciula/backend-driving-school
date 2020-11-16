package pl.superjazda.drivingschool.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.superjazda.drivingschool.exception.EmailAlreadyExistsException;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.exception.UserAlreadyExistsException;
import pl.superjazda.drivingschool.jwt.JwtTokenUtil;
import pl.superjazda.drivingschool.jwt.JwtUserDetails;
import pl.superjazda.drivingschool.jwt.TokenDto;
import pl.superjazda.drivingschool.role.Role;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.role.RoleType;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserDto;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private AuthenticationManager authManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtil tokenUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.authManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtil = jwtTokenUtil;
    }

    public TokenDto logInUser(Login login) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenUtil.generateToken(auth);

        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new TokenDto(jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getSurname(),
                roles);
    }

    public UserDto registerUser(Register register) {
        if (userRepository.existsByUsername(register.getUsername())) {
            throw new UserAlreadyExistsException("Username exists in database");
        }

        if (userRepository.existsByEmail(register.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists in database");
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

        return new UserDto(userRepository.save(user));
    }
}
