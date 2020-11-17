package pl.superjazda.drivingschool.authentication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.superjazda.drivingschool.exception.EmailAlreadyExistsException;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.exception.UserAlreadyExistsException;
import pl.superjazda.drivingschool.jwt.JwtTokenUtil;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AuthenticationService authService;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldUserExistsExceptionWhenRegisterUserTest() {
        exceptionRule.expect(UserAlreadyExistsException.class);
        exceptionRule.expectMessage("Username exists in database");
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        authService.registerUser(initRegisterData());

        verify(userRepository).existsByUsername("username");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldEmailExistsExceptionWhenRegisterUserTest() {
        exceptionRule.expect(EmailAlreadyExistsException.class);
        exceptionRule.expectMessage("Email already exists in database");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        authService.registerUser(initRegisterData());

        verify(userRepository).existsByUsername("username");
        verify(userRepository).existsByEmail("email@domain.com");
    }

    @Test
    public void shouldRoleNotFoundWhenRegisterUserTest() {
        exceptionRule.expect(RoleNotFoundException.class);
        exceptionRule.expectMessage("Role not found");
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        authService.registerUser(initRegisterData());
    }

    private Register initRegisterData() {
        return new Register("username", "email@domain.com", "username123", "Joe", "Doe");
    }
}
