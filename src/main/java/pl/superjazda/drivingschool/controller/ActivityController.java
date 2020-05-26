package pl.superjazda.drivingschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.model.Activity;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.ActivityDto;
import pl.superjazda.drivingschool.repository.ActivityRepository;
import pl.superjazda.drivingschool.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/activity")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityController {
    private ActivityRepository activityRepository;
    private UserRepository userRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/course/{id}/signup")
    public ResponseEntity<?> signUserForActivity(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        Activity activity = findActivityById(id);

        activity.setStudent(user);
        activityRepository.save(activity);

        return ResponseEntity.ok(new ActivityDto(activity));
    }

    @GetMapping("/course/{id}/student")
    public ResponseEntity<?> findSignInUserActivitiesByCourse(@PathVariable Long id) {
        List<Activity> activities = activityRepository.findAllByCourseIdAndStudentNotNull(id);
        List<ActivityDto> dtos = new ArrayList<>();

        activities.forEach(activity -> {
            dtos.add(new ActivityDto(activity));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> findActivitiesByCourseId(@PathVariable Long id) {
        List<Activity> activities = activityRepository.findAllByCourseId(id);
        List<ActivityDto> dtos = new ArrayList<>();

        activities.forEach(activity -> {
            dtos.add(new ActivityDto(activity));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{id}/mine")
    public ResponseEntity<?> findActivitiesByUserAndCourseId(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Activity> activities = activityRepository.findAllByCourseIdAndStudentUsername(id, username);
        List<ActivityDto> dtos = new ArrayList<>();

        activities.forEach(activity -> {
            dtos.add(new ActivityDto(activity));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{id}/dates")
    public ResponseEntity<?> findActivityDatesByCourseId(@PathVariable Long id) {
        List<Activity> activities = activityRepository.findAllByCourseIdOrderByDateOfActivity(id);
        List<Date> activityDates = new ArrayList<>();

        activities.forEach(activity -> {
            activityDates.add(activity.getDateOfActivity());
        });

        return ResponseEntity.ok(activityDates);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/{id}/rate/{rate}")
    public ResponseEntity<?> addRate(@PathVariable Long id, @PathVariable int rate) {
        Activity activity = findActivityById(id);
        activity.setRate(rate);
        activityRepository.save(activity);

        return ResponseEntity.ok(new ActivityDto(activity));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        Activity activity = findActivityById(id);
        activity.setComment(comment);
        activityRepository.save(activity);

        return ResponseEntity.ok(new ActivityDto(activity));
    }

    private Activity findActivityById(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Activity not found"));
        return activity;
    }
}
