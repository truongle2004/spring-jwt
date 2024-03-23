package com.example.todoapp.repository;

import com.example.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Boolean existsByEmail(String email);
     Optional<User> findByUsernameOrEmail(String username, String email);
}
