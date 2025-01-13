package student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserJson(
        @JsonProperty("id")
        String name,
        @JsonProperty("spendDate")
        String password
) {
}
