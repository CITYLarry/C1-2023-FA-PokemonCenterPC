package co.com.sofka.pokemoncenterpc.domain.collection;

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
    private String pkdxNumber;
    private String name;
    private String nickname = name;
    private List<String> typeList;
    private Boolean inTeam = false;
}
