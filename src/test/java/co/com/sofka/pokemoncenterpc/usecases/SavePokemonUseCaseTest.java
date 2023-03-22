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
class SavePokemonUseCaseTest {

    @Mock
    IPokemonRepository pokemonRepository;

    ModelMapper modelMapper;
    SavePokemonUseCase savePokemonUseCase;

    @BeforeEach
    void setup() {
        modelMapper = new ModelMapper();
        savePokemonUseCase = new SavePokemonUseCase(pokemonRepository, modelMapper);
    }

    @Test
    @DisplayName("Save pokemon successfully")
    void successScenario() {

        Pokemon p1 = new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false);

        Mockito.when(pokemonRepository.save(new Pokemon("testId", "testNmbr", "testName", "testNick", List.of("testType"), false)))
                .thenReturn(Mono.just(p1));

        var result = savePokemonUseCase.save(modelMapper.map(p1, PokemonDTO.class));

        StepVerifier.create(result)
                .expectNext(new PokemonDTO("testId", "testNmbr", "testName", "testNick", List.of("testType"), false))
                .verifyComplete();
    }
}