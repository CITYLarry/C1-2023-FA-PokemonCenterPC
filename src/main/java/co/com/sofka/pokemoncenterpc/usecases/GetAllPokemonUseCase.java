package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import co.com.sofka.pokemoncenterpc.usecases.interfaces.IGetAllPokemon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetAllPokemonUseCase implements IGetAllPokemon {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;

    @Override
    public Flux<PokemonDTO> getAll() {
        return pokemonRepository
                .findAll()
                .switchIfEmpty(Flux.empty())
                .map(pkmn -> modelMapper.map(pkmn, PokemonDTO.class));
    }
}
