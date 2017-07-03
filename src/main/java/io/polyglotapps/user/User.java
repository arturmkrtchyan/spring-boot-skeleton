package io.polyglotapps.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isEmailVerified = Boolean.FALSE;

    @JsonIgnore
    private String emailVerificationCode;

    @Embedded
    private Credentials credentials;

    public User() {
        this(null, null, null);
    }

    public User(final String fullName, final Credentials credentials) {
        this(null, fullName, credentials);
    }

    public User(final Long id, final String fullName, final Credentials credentials) {
        this.id = id;
        this.fullName = fullName;
        this.credentials = credentials;
    }

    public void merge(final User source) {

        if(source.getFullName() != null) {
            this.setFullName(source.getFullName());
        }

        if(source.getCredentials() != null && source.getCredentials().getPassword() != null) {
            this.getCredentials().setPassword(source.getCredentials().getPassword());
        }

    }

}
