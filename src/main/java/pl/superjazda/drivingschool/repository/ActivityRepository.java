package pl.superjazda.drivingschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.superjazda.drivingschool.model.Activity;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByCourseIdAndStudentNotNull(Long id);
    List<Activity> findAllByCourseId(Long id);
    List<Activity> findAllByCourseIdAndStudentUsername(Long id, String username);
    List<Activity> findAllByCourseIdOrderByDateOfActivity(Long id);
}
