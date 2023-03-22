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
class DeletePokemonUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;

    ModelMapper modelMapper;
    DeletePokemonUseCase deletePokemonUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        deletePokemonUseCase = new DeletePokemonUseCase(pokemonRepository, modelMapper);
    }

    @Test
    @DisplayName("Delete pokemon successfully")
    void successScenario() {

        Pokemon p1 = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(p1));
        Mockito.when(pokemonRepository.deleteById("testId")).thenReturn(Mono.empty());

        var result = deletePokemonUseCase.delete("testId");

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        Mockito.verify(pokemonRepository).findById("testId");
        Mockito.verify(pokemonRepository).deleteById("testId");
    }

    @Test
    @DisplayName("Delete pokemon fail")
    void failScenario() {

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.empty());

        var result = deletePokemonUseCase.delete("testId");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("No pokemon found for id testId"))
                .verify();

        Mockito.verify(pokemonRepository).findById("testId");
    }
}