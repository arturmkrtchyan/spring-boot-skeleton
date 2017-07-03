package io.polyglotapps.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class Credentials {

    @Column(unique=true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Credentials() {
        this(null, null);
    }

    public Credentials(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Credentials(final String email) {
        this(email, null);
    }

}
