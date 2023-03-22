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
class GetPokemonByIdUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;

    ModelMapper modelMapper;
    GetPokemonByIdUseCase getPokemonByIdUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        getPokemonByIdUseCase = new GetPokemonByIdUseCase(pokemonRepository, modelMapper);
    }

    @Test
    @DisplayName("Get pokemon by Id successfully")
    void successScenario() {

        Pokemon p1 = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.just(p1));

        var result = getPokemonByIdUseCase.getPokemon("testId");

        StepVerifier.create(result)
                .expectNext(new PokemonDTO("testId", "testNmbr", "testName", "testNick", List.of("testType"), false))
                .verifyComplete();

        Mockito.verify(pokemonRepository).findById("testId");
    }

    @Test
    @DisplayName("Get pokemon - pokemon not found")
    void failScenario() {

        Mockito.when(pokemonRepository.findById("testId")).thenReturn(Mono.empty());

        var result = getPokemonByIdUseCase.getPokemon("testId");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("No pokemon found for id testId"))
                .verify();

        Mockito.verify(pokemonRepository).findById("testId");
    }
}