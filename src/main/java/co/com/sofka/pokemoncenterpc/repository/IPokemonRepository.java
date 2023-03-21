package co.com.sofka.pokemoncenterpc.repository;

import co.com.sofka.pokemoncenterpc.domain.collection.Pokemon;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPokemonRepository extends ReactiveMongoRepository<Pokemon, String> {
}
