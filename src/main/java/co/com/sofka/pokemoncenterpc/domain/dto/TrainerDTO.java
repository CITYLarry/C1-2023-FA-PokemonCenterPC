package co.com.sofka.pokemoncenterpc.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainerDTO {
    private String trnrId;
    private String name;
    private String pokeDollar;
    private List<PokemonDTO> pokemonTeam;
}
