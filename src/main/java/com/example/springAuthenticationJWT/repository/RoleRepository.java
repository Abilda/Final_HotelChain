package com.example.springAuthenticationJWT.repository;

import com.example.springAuthenticationJWT.models.ERole;
import com.example.springAuthenticationJWT.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
