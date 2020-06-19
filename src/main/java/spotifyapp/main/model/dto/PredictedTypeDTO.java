package spotifyapp.main.model.dto;

import java.util.Arrays;

public enum PredictedTypeDTO {
    BLUES("blues"),
    CLASSICAL("classical"),
    COUNTRY("country"),
    DISCO("disco"),
    HIPHOP("hiphop"),
    JAZZ("jazz"),
    METAL("metal"),
    POP("pop"),
    REGGAE("reggae"),
    ROCK("rock"),
    UNKNOWN("unknown");

    private String name;

    PredictedTypeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PredictedTypeDTO get(String name) {
        return Arrays.stream(values())
                .filter(predictedTypeDTO -> predictedTypeDTO.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No type of " + name + " found!"));
    }
}
