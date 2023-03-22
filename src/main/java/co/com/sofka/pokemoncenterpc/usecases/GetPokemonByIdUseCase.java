package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import co.com.sofka.pokemoncenterpc.usecases.interfaces.IGetPokemonById;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetPokemonByIdUseCase  implements IGetPokemonById {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;


    @Override
    public Mono<PokemonDTO> getPokemon(String pkmnId) {
        return pokemonRepository
                .findById(pkmnId)
                .switchIfEmpty(Mono.error(new RuntimeException("No pokemon found for id " + pkmnId)))
                .map(pkmn -> modelMapper.map(pkmn, PokemonDTO.class));
    }
}
