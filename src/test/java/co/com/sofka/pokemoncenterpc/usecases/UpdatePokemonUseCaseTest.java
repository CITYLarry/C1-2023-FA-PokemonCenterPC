package co.com.sofka.pokemoncenterpc.usecases;

import co.com.sofka.pokemoncenterpc.domain.collection.Pokemon;
import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.repository.IPokemonRepository;
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

@ExtendWith(MockitoExtension.class)
class UpdatePokemonUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;

    ModelMapper modelMapper;
    UpdatePokemonUseCase updatePokemonUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        updatePokemonUseCase = new UpdatePokemonUseCase(pokemonRepository, modelMapper);
    }

    @Test
    @DisplayName("Update pokemon successfully")
    void successScenario() {

        Pokemon p1 = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(p1));

        Pokemon p2 = new Pokemon("testId", "testNmbr2", "testName2", "testNick2", List.of("testType2"), true);

        Mockito.when(pokemonRepository.save(p2)).thenReturn(Mono.just(p2));

        var result = updatePokemonUseCase.update(
                "testId",
                new PokemonDTO("testId", "testNmbr2", "testName2", "testNick2", List.of("testType2"), true)
        );

        StepVerifier.create(result)
                .expectNext(new PokemonDTO("testId", "testNmbr2", "testName2", "testNick2", List.of("testType2"), true))
                .verifyComplete();

        Mockito.verify(pokemonRepository).findById("testId");
    }

    @Test
    @DisplayName("Update pokemon - pokemon not found")
    void failScenario() {

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.empty());

        var result = updatePokemonUseCase.update("testId", new PokemonDTO());

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("No pokemon found for id testId"))
                .verify();

        Mockito.verify(pokemonRepository).findById("testId");
    }
}