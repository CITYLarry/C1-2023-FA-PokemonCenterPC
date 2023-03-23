package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.collection.Pokemon;
import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.publisher.PokemonPublisher;
import co.com.sofka.pokemoncenterpc.publisher.TransferPublisher;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransferToPcUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;
    @Mock
    TransferPublisher transferPublisher;

    ModelMapper modelMapper;
    TransferToPcUseCase transferToPcUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        transferToPcUseCase = new TransferToPcUseCase(pokemonRepository, modelMapper, transferPublisher);
    }

    @Test
    @DisplayName("Transfer pokemon to pc successfully")
    void successScenario() {

        Pokemon pokemon = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), true);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(pokemon));

        Pokemon pokemonInTeam = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.save(pokemonInTeam)).thenReturn(Mono.just(pokemonInTeam));

        var result = transferToPcUseCase.transferToPc("trainer1", "testId");

        StepVerifier.create(result)
                .expectNextMatches(pokemonDTO -> pokemonDTO.getInTeam().equals(false))
                .verifyComplete();

        Mockito.verify(pokemonRepository).findById(any(String.class));
        Mockito.verify(pokemonRepository).save(any(Pokemon.class));
        try {
            Mockito.verify(transferPublisher).publish(any(String.class), any(PokemonDTO.class));
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Test
    @DisplayName("Transfer pokemon - pokemon is not in team")
    void failScenario() {

        Pokemon pokemonInTeam = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(pokemonInTeam));

        var result = transferToPcUseCase.transferToPc("trainer1", "testId");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Pokemon for id testId is not in a team"))
                .verify();

        Mockito.verify(pokemonRepository).findById(any(String.class));
    }
}