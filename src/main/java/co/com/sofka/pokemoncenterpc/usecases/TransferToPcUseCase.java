package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.publisher.TransferPublisher;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransferToPcUseCase {

    private final IPokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;
    private final TransferPublisher transferPublisher;

    public Mono<PokemonDTO> transferToPc(String trnrId, String pkmnId) {
        return pokemonRepository
                .findById(pkmnId)
                .switchIfEmpty(Mono.error(new RuntimeException("No pokemon found for id " + pkmnId)))
                .flatMap(pkmn -> {
                    if (!pkmn.getInTeam()){
                        return Mono.error(new RuntimeException("Pokemon for id " + pkmn.getPkmnId() + " is not in a team"));
                    }
                    pkmn.setInTeam(false);
                    return pokemonRepository.save(pkmn);
                })
                .map(pkmn -> modelMapper.map(pkmn, PokemonDTO.class))
                .doOnSuccess(pokemonDTO -> {
                    try {
                        transferPublisher.publish(trnrId ,pokemonDTO);
                    } catch (JsonProcessingException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }
}
