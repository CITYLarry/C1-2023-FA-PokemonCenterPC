package co.com.sofka.pokemoncenterpc.router;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.usecases.GetAllPokemonUseCase;
import co.com.sofka.pokemoncenterpc.usecases.SavePokemonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PokemonRouter {

    @Bean
    public RouterFunction<ServerResponse> getAllPokemon(GetAllPokemonUseCase getAllPokemonUseCase) {
        return route(
                GET("/pokemon_pc"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllPokemonUseCase.getAll(), PokemonDTO.class))
                        .onErrorResume(throwable -> ServerResponse.noContent().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> savePokemon(SavePokemonUseCase savePokemonUseCase) {
        return route(
                POST("/pokemon_pc").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(PokemonDTO.class)
                        .flatMap(pokemonDTO -> savePokemonUseCase.save(pokemonDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.badRequest().build()))
        );
    }
}
