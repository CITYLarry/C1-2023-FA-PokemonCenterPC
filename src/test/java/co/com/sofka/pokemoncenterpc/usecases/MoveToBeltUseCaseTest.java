package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.collection.Pokemon;
import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.publisher.PokemonPublisher;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MoveToBeltUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;
    @Mock
    PokemonPublisher pokemonPublisher;

    ModelMapper modelMapper;
    MoveToBeltUseCase moveToBeltUseCase;


    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        moveToBeltUseCase = new MoveToBeltUseCase(pokemonRepository, modelMapper, pokemonPublisher);
    }

    @Test
    @DisplayName("Move pokemon to belt successfully")
    void successScenario() {

        Pokemon pokemon = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(pokemon));

        Pokemon pokemonInTeam = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), true);

        Mockito.when(pokemonRepository.save(pokemonInTeam)).thenReturn(Mono.just(pokemonInTeam));

        var result = moveToBeltUseCase.moveToBelt("trainer1", "testId");

        StepVerifier.create(result)
                .expectNextMatches(pokemonDTO -> pokemonDTO.getInTeam().equals(true))
                .verifyComplete();

        Mockito.verify(pokemonRepository).findById(any(String.class));
        Mockito.verify(pokemonRepository).save(any(Pokemon.class));
        try {
            Mockito.verify(pokemonPublisher).publish(any(String.class), any(PokemonDTO.class));
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Test
    @DisplayName("Move pokemon to belt - pokemon already in team")
    void failScenario() {

        Pokemon pokemonInTeam = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), true);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(pokemonInTeam));

        var result = moveToBeltUseCase.moveToBelt("trainer1", "testId");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Pokemon for id testId is already in a team"))
                .verify();

        Mockito.verify(pokemonRepository).findById(any(String.class));
    }
}