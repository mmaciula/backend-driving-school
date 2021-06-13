package pl.superjazda.drivingschool.api.practical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticalRepository extends JpaRepository<Practical, Long> {
    List<Practical> findAllByCourseId(Long id);
    List<Practical> findAllByStudentUsername(String username);
    List<Practical> findAllByInstructorUsernameAndStudentNotNull(String username);
    List<Practical> findAllByCourseIdOrderByDate(Long id);
}
