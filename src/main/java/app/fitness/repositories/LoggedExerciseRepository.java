package app.fitness.repositories;

import app.fitness.implementations.LoggedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggedExerciseRepository extends JpaRepository<LoggedExercise, Long> {
    LoggedExercise findLoggedExerciseByIdAndNameAndDate(Long id, String name, String date);
    List<LoggedExercise> findAllById(Long id);
}
