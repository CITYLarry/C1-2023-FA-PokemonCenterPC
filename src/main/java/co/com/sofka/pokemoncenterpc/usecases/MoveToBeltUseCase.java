package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.publisher.PokemonPublisher;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MoveToBeltUseCase {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;
    private final PokemonPublisher pokemonPublisher;

    public Mono<PokemonDTO> moveToBelt(String trnrId, String pkmnId) {
        return pokemonRepository
                .findById(pkmnId)
                .switchIfEmpty(Mono.error(new RuntimeException("No pokemon found for id " + pkmnId)))
                .flatMap(pkmn -> {
                    if (pkmn.getInTeam()){
                        return Mono.error(new RuntimeException("Pokemon for id " + pkmn.getPkmnId() + " is already in a team"));
                    }
                    pkmn.setInTeam(true);
                    return pokemonRepository.save(pkmn);
                })
                .map(pkmn -> modelMapper.map(pkmn, PokemonDTO.class))
                .doOnSuccess(pokemonDTO -> {
                    try {
                        pokemonPublisher.publish(trnrId ,pokemonDTO);
                    } catch (JsonProcessingException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }
}
