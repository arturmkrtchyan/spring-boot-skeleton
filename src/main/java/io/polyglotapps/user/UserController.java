package io.polyglotapps.user;

import io.polyglotapps.system.ResourceNotFoundException;
import io.polyglotapps.system.security.AuthenticationFailureException;
import io.polyglotapps.system.security.AuthorizationFailureException;
import io.polyglotapps.system.security.JwtService;
import io.polyglotapps.system.security.SecurityContextAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/v1/users")
@Slf4j
class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SecurityContextAccessor securityContextAccessor;

    @GetMapping("/me")
    @ResponseBody
    User getCurrentUser() {
        final String email = (String) securityContextAccessor.getContext().getAuthentication().getPrincipal();
        return userService.getUserByEmail(email).orElseThrow(AuthorizationFailureException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createUser(@RequestBody final User user) {

        final User createdUser = userService.createUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getId()).toUri();

        final Optional<User> user1 = userService.getUserByEmail(createdUser.getCredentials().getEmail());
        log.info(user1.map(User::toString).orElse("NULL"));

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void patchUser(@PathVariable("id") final Long id, @RequestBody final User user) {
        userService.updateUser(id, user);
    }

    @PostMapping("/email-verification-request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void emailVerificationRequest(@RequestBody final String token) {
        final User user = userService.verifyEmail(token);
        if(user == null) {
            throw new ResourceNotFoundException(token, "Token is not found");
        }
    }

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    Token createToken(@RequestBody final Credentials credentials) throws AuthenticationFailureException {
        if(userService.tryAuthentication(credentials)) {
            final String token = jwtService.createToken(credentials.getEmail());
            jwtService.getUsernameFromToken(token);
            return new Token(token);
        }
        throw new AuthenticationFailureException();
    }


    //TODO VERIFY EMAIL
    //TODO RESET PASSWORD (TOKEN + RESET)

    //TODO UPDATE USER



}
