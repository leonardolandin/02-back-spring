package com.br.back02.repository;

import com.br.back02.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Override
    Optional<User> findById(String id);

    Optional<User> findByEmailAndPasswordAndActiveIsTrue(String email, String password);

    Optional<User> findByEmailAndActiveIsTrue(String email);
}
