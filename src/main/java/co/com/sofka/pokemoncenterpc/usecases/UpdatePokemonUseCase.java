package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.collection.Pokemon;
import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import co.com.sofka.pokemoncenterpc.usecases.interfaces.IUpdatePokemon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdatePokemonUseCase implements IUpdatePokemon {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<PokemonDTO> update(String pkmnId, PokemonDTO pokemonDTO) {
        return pokemonRepository
                .findById(pkmnId)
                .switchIfEmpty(Mono.error(new RuntimeException("No pokemon found for id " + pkmnId)))
                .flatMap(pkmn -> {
                    pokemonDTO.setPkmnId(pkmn.getPkmnId());
                    return pokemonRepository.save(modelMapper.map(pokemonDTO, Pokemon.class));
                })
                .map(pkmn ->modelMapper.map(pkmn, PokemonDTO.class));
    }
}
