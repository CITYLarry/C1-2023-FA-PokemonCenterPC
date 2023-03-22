package co.com.sofka.pokemoncenterpc.usecases.interfaces;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetPokemonById {
    Mono<PokemonDTO> getPokemon(String pkmnId);
}
