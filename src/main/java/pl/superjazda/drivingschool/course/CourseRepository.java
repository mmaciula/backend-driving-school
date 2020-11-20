package pl.superjazda.drivingschool.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);
    List<Course> findAllByInstructorUsername(String username);
    Course findByName(String name);
}
