package co.com.sofka.pokemoncenterpc.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDTO {

    private String pkmnId;
    @NotNull(message = "Pokedex number is necessary")
    @NotBlank(message = "Pokdex number is empty")
    private String pkdxNumber;
    @NotNull(message = "Pokemon name is necessary")
    @NotBlank(message = "Pokemon name is empty")
    private String name;
    private String nickname;
    @NotBlank(message = "Pokemon needs a least one type, check your Pokedex")
    private List<String> typeList;
    private Boolean inTeam;
}
