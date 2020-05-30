package pl.superjazda.drivingschool.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAllByCourseId(Long id);
    List<Exam> findAllByStudentUsername(String username);
    List<Exam> findAllByInstructorUsername(String username);
}
