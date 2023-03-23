package co.com.sofka.pokemoncenterpc.publisher;

import co.com.sofka.pokemoncenterpc.domain.dto.PokemonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PokemonEvent {

    private String trnrId;
    private PokemonDTO pokemonToMove;
    private String eventName; //moveToBelt, moveToPc
}
