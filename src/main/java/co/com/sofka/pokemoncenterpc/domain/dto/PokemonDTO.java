package co.com.sofka.pokemoncenterpc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDTO {

    private String id;
    private String pkdxNumber;
    private String name;
    private List<String> typeList;
    private Boolean inTeam;
}
