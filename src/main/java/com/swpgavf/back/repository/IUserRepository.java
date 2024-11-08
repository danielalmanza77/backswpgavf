package com.swpgavf.back.repository;

import com.swpgavf.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by their username
    User findByUsername(String username);
}
