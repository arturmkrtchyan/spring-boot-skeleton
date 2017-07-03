package io.polyglotapps.user;


import io.polyglotapps.system.EntityCreatedEvent;
import io.polyglotapps.system.security.AuthorizationFailureException;
import io.polyglotapps.system.security.SecurityContextAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityContextAccessor securityContextAccessor;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    User createUser(final User user) {

        //encode password before saving.
        final String plainPassword = user.getCredentials().getPassword();
        final String encodedPassword = passwordEncoder.encode(plainPassword);
        user.getCredentials().setPassword(encodedPassword);

        // create email verification code
        user.setEmailVerificationCode(UUID.randomUUID().toString());

        final User createdUser = repository.save(user);

        // TODO email needs to be sent via listener
        eventPublisher.publishEvent(new EntityCreatedEvent<User>(createdUser));

        return createdUser;
    }

    User updateUser(final Long id, final User user) {
        final String email = (String) securityContextAccessor.getContext().getAuthentication().getPrincipal();
        final User persistedUser = repository.findByCredentialsEmail(email)
                .orElseThrow(AuthorizationFailureException::new);

        // TODO move to spring security
        if(!persistedUser.getId().equals(id)) {
            throw new AuthorizationFailureException();
        }

        persistedUser.merge(user);
        return repository.save(persistedUser);
    }

    User verifyEmail(final String token) {
        final Optional<User> user = repository.findByEmailVerificationCode(token);
        if(user.isPresent() && token.equals(user.get().getEmailVerificationCode())) {
                user.get().setEmailVerificationCode(null);
                user.get().setIsEmailVerified(Boolean.TRUE);
                return repository.save(user.get());
        }
        return null;
    }

    Optional<User> getUserByEmail(final String email) {
        return repository.findByCredentialsEmail(email);
    }

    boolean tryAuthentication(final Credentials credentials) {
        log.info(credentials.toString());
        final Optional<User> userResult = getUserByEmail(credentials.getEmail());
        return userResult.filter(user -> passwordEncoder.matches(credentials.getPassword(),
                user.getCredentials().getPassword())).isPresent();
    }

}
