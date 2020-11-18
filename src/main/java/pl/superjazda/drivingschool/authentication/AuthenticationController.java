package pl.superjazda.drivingschool.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;
import pl.superjazda.drivingschool.jwt.TokenDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    private  AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> authenticateUser(@Valid @RequestBody Login logIn) {
        TokenDto authenticatedUser = authService.logInUser(logIn);

        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> registerUser(@Valid @RequestBody Register register) {
        authService.registerUser(register);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }
}
