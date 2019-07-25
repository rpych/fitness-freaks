package app.fitness.repositories;

import app.fitness.implementations.BodyParameters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyParametersRepository extends CrudRepository<BodyParameters, Long> {

}

