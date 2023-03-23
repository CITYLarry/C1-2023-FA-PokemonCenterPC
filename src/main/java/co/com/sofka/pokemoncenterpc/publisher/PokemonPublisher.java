package co.com.sofka.pokemoncenterpc.publisher;

import co.com.sofka.pokemoncenterpc.config.RabbitMQConfig;
import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PokemonPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public PokemonPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String trnrId, PokemonDTO pokemonDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(new PokemonEvent(trnrId, pokemonDTO, "Move to belt"));
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POKEMON_EXCHANGE,
                RabbitMQConfig.POKEMON_ROUTING_KEY,
                message
        );
    }
}
