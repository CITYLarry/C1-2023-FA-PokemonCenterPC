package co.com.sofka.pokemoncenterpc.domain.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pokemon_center_pc")
public class Pokemon {

    @Id
    private String pkmnId;
    @NotNull(message = "Pokedex number is necessary")
    @NotBlank(message = "Pokdex number is empty")
    private String pkdxNumber;
    @NotNull(message = "Pokemon name is necessary")
    @NotBlank(message = "Pokemon name is empty")
    private String name;
    private String nickname = name;
    @NotEmpty(message = "Pokemon needs a least one type, check your Pokedex")
    private List<String> typeList;
    private Boolean inTeam = false;
}
