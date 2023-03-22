package co.com.sofka.pokemoncenterpc.usecases.interfaces;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IUpdatePokemon {
    Mono<PokemonDTO> update(String pkmnId, PokemonDTO pokemonDTO);
}
