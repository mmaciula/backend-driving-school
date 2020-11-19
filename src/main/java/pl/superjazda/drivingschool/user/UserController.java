package pl.superjazda.drivingschool.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PostAuthorize("returnObject.getUsername() == authentication.principal.username")
    public UserDto showLogInUser() {
        return userService.showLoggedUser();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> users = userService.findAllRegisteredUsers();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/course/add/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> assignCourseToUser(@PathVariable Long id) {
        UserDto user = userService.assignUserToCourse(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/roles/assign/{role}/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> assignRoleToUser(@PathVariable String role, @PathVariable String username) {
        UserDto user = userService.assignRoleToUser(role, username);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);

        return ResponseEntity.ok(new ResponseMessage("User: " + username + " deleted successfully"));
    }
}
