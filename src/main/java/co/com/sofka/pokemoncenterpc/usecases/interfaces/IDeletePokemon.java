package co.com.sofka.pokemoncenterpc.usecases.interfaces;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IDeletePokemon {
    Mono<Void> delete(String pkmnId);
}
