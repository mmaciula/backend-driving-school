package pl.superjazda.drivingschool.practical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticalRepository extends JpaRepository<Practical, Long> {
    List<Practical> findAllByCourseIdAndStudentNotNull(Long id);
    List<Practical> findAllByCourseId(Long id);
    List<Practical> findAllByCourseIdAndStudentUsername(Long id, String username);
    List<Practical> findAllByCourseIdOrderByDate(Long id);
}
