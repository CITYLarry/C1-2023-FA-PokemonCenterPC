package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import co.com.sofka.pokemoncenterpc.usecases.interfaces.IDeletePokemon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeletePokemonUseCase implements IDeletePokemon {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<Void> delete(String pkmnId) {
        return pokemonRepository
                .findById(pkmnId)
                .switchIfEmpty(Mono.error(new RuntimeException("No pokemon found for id " + pkmnId)))
                .flatMap(pkmn -> pokemonRepository.deleteById(pkmn.getPkmnId()));
    }
}
