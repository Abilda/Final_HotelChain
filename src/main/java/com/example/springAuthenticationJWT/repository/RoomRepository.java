package com.example.springAuthenticationJWT.repository;

import com.example.springAuthenticationJWT.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
