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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GetAllPokemonUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;

    ModelMapper modelMapper;
    GetAllPokemonUseCase getAllPokemonUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        getAllPokemonUseCase = new GetAllPokemonUseCase(pokemonRepository, modelMapper);
    }

    @Test
    @DisplayName("Get all pokemon successfully")
    void successScenario() {

        Pokemon p1 = new Pokemon("testId1", "testNmbr1", "testName1", "testNick1", List.of("testType1"), false);
        Pokemon p2 = new Pokemon("testId2", "testNmbr2", "testName2", "testNick2", List.of("testType2"), true);

        Mockito.when(pokemonRepository.findAll()).thenReturn(Flux.just(p1, p2));

        var result = getAllPokemonUseCase.getAll();

        StepVerifier.create(result)
                .expectNext(new PokemonDTO("testId1", "testNmbr1", "testName1", "testNick1", List.of("testType1"), false))
                .expectNext(new PokemonDTO("testId2", "testNmbr2", "testName2", "testNick2", List.of("testType2"), true))
                .verifyComplete();

        Mockito.verify(pokemonRepository).findAll();
    }
}