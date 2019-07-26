package app.fitness.repositories;

import app.fitness.implementations.DailyExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyExerciseRepository extends JpaRepository<DailyExercise, Long> {
    List<DailyExercise> getDailyExercisesByUserId(Long id);
}