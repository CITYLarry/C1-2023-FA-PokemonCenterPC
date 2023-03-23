package co.com.sofka.pokemoncenterpc.router;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import co.com.sofka.pokemoncenterpc.domain.dto.TrainerDTO;
import co.com.sofka.pokemoncenterpc.usecases.DeletePokemonUseCase;
import co.com.sofka.pokemoncenterpc.usecases.GetAllPokemonUseCase;
import co.com.sofka.pokemoncenterpc.usecases.GetPokemonByIdUseCase;
import co.com.sofka.pokemoncenterpc.usecases.MoveToBeltUseCase;
import co.com.sofka.pokemoncenterpc.usecases.SavePokemonUseCase;
import co.com.sofka.pokemoncenterpc.usecases.TransferToPcUseCase;
import co.com.sofka.pokemoncenterpc.usecases.UpdatePokemonUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PokemonRouter {

    private WebClient trainersAPI;

    public PokemonRouter() {
        trainersAPI = WebClient.create("http://localhost:8081");
    }

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
    public RouterFunction<ServerResponse> getPokemonById(GetPokemonByIdUseCase getPokemonByIdUseCase) {
        return route(
                GET("/pokemon_pc/{pkmnId}"),
                request -> getPokemonByIdUseCase.getPokemon(request.pathVariable("pkmnId"))
                        .flatMap(pokemonDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(pokemonDTO))
                        .onErrorResume(throwable -> ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(throwable.getMessage()))
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
                                .onErrorResume(throwable -> {
                                    if (throwable instanceof ConstraintViolationException ex) {
                                        List<String> errors = ex.getConstraintViolations().stream()
                                                .map(ConstraintViolation::getMessage)
                                                .toList();
                                        return ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(errors);
                                    } else {
                                        return ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(throwable.getMessage());
                                    }
                                }))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> movePokemonToBelt(MoveToBeltUseCase moveToBeltUseCase) {
        return route(
                POST("/pokemon_pc/{pkmnId}/move_to_belt/{trnrId}"),
                request -> trainersAPI.get()
                        .uri("/trainers/" + request.pathVariable("trnrId"))
                        .retrieve()
                        .bodyToMono(TrainerDTO.class)
                        .flatMap(trainerDTO -> moveToBeltUseCase.moveToBelt(trainerDTO.getTrnrId(), request.pathVariable("pkmnId"))
                                .flatMap(pokemonDTO -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(pokemonDTO))
                                .onErrorResume(throwable -> ServerResponse.badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(throwable.getMessage())))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> transferPokemonToPc(TransferToPcUseCase transferToPcUseCase) {
        return route(
                POST("/pokemon_pc/{pkmnId}/transfer_to_pc/{trnrId}"),
                request -> trainersAPI.get()
                        .uri("/trainers/" + request.pathVariable("trnrId"))
                        .retrieve()
                        .bodyToMono(TrainerDTO.class)
                        .flatMap(trainerDTO -> transferToPcUseCase.transferToPc(trainerDTO.getTrnrId(), request.pathVariable("pkmnId"))
                                .flatMap(pokemonDTO -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(pokemonDTO))
                                .onErrorResume(throwable -> ServerResponse.badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(throwable.getMessage())))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> updatePokemon(UpdatePokemonUseCase updatePokemonUseCase) {
        return route(
                PUT("/pokemon_pc/{pkmnId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(PokemonDTO.class)
                        .flatMap(pokemonDTO -> updatePokemonUseCase.update(request.pathVariable("pkmnId"), pokemonDTO)
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> {
                                    if (throwable instanceof ConstraintViolationException ex) {
                                        List<String> errors = ex.getConstraintViolations().stream()
                                                .map(ConstraintViolation::getMessage)
                                                .toList();
                                        return ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(errors);
                                    } else {
                                        return ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(throwable.getMessage());
                                    }
                                }))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> deletePokemon(DeletePokemonUseCase deletePokemonUseCase) {
        return route(
                DELETE("/pokemon_pc/{pkmnId}"),
                request -> deletePokemonUseCase.delete(request.pathVariable("pkmnId"))
                        .thenReturn(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("Deleted Pokemon with id: " + request.pathVariable("pkmnId")))
                        .flatMap(response -> response)
                        .onErrorResume(throwable -> ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(throwable.getMessage()))
        );
    }
}
