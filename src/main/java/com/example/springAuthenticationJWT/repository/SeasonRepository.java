package com.example.springAuthenticationJWT.repository;

import com.example.springAuthenticationJWT.models.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
}
