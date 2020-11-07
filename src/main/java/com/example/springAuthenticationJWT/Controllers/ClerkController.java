package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.ERole;
import com.example.springAuthenticationJWT.models.Reservation;
import com.example.springAuthenticationJWT.models.Role;
import com.example.springAuthenticationJWT.models.User;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.ReservationRepository;
import com.example.springAuthenticationJWT.repository.RoomRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/clerkdesk")
public class ClerkController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/getAllGuests")
    public ResponseEntity<?> getAllGuests(@RequestHeader("authorization") String authHeader) {
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        boolean isModerator = false;
        for (Role role : user.get().getRoles())
            if (role.getName() == ERole.ROLE_MODERATOR) {
                isModerator = true;
                break;
            }
        if (!isValidJWT || !isModerator) {
            return ResponseEntity.ok("You are not authorized to access Clerks area");
        }
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/getActiveReservationsForUser")
    public ResponseEntity<?> getReservations(@RequestHeader("authorization") String authHeader, @PathParam("Id") Long Id){
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        boolean isModerator = false;
        for (Role role : user.get().getRoles())
            if (role.getName() == ERole.ROLE_MODERATOR) {
                isModerator = true;
                break;
            }
        if (!isValidJWT || !isModerator) {
            return ResponseEntity.ok("You are not authorized to access Clerks area");
        }
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Long> reservationsForUser = new ArrayList<>();
        Date current = new Date();
        for (Reservation reservation : allReservations)
            if (reservation.getCheckout().compareTo(current) > 0 && reservation.getUser().getId() == Id)
                reservationsForUser.add(reservation.getId());
        return ResponseEntity.ok(reservationsForUser);
    }
}
