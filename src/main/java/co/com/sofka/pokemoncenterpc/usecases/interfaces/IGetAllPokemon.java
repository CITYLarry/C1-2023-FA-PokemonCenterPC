package co.com.sofka.pokemoncenterpc.usecases.interfaces;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface IGetAllPokemon {
    Flux<PokemonDTO> getAll();
}
