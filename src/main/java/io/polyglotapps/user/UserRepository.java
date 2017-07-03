package io.polyglotapps.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByCredentialsEmail(String email);
    Optional<User> findByEmailVerificationCode(String token);
}