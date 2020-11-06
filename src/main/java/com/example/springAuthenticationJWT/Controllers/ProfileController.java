package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.Reservation;
import com.example.springAuthenticationJWT.models.User;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.ReservationRepository;
import com.example.springAuthenticationJWT.repository.RoomRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.incrementer.HanaSequenceMaxValueIncrementer;
import org.springframework.web.bind.annotation.*;

import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/profile")
public class ProfileController {
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

    @GetMapping("/getallbookings")
    public ResponseEntity<?> getAllBookings(@RequestHeader("authorization") String authHeader) {
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        if (!isValidJWT)
            return ResponseEntity.ok("Autorization error :(. Pls try to (re)login");
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Reservation> pastReservations = new ArrayList<>(), bookings = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            java.util.Date currentDate = new java.util.Date();
            if (reservation.getUser().getId() == user.get().getId())
                if (reservation.getCheckout().compareTo(currentDate) <= 0) {
                    pastReservations.add(reservation);
                }
                else {
                    bookings.add(reservation);
                }
        }
        for (Reservation pastreservation : pastReservations)
            System.out.println("past reservation - " + pastreservation.getUser().getUsername()
                    + " at time - " + pastreservation.getCheckout());
        for (Reservation futureresrvation : bookings)
            System.out.println("bookings " + futureresrvation.getUser().getUsername() +
                    "at time " + futureresrvation.getCheckin());
        HashMap<String, List<Reservation>> jsonAns = new HashMap<>();
        jsonAns.put("past reservations: ", pastReservations);
        jsonAns.put("upcoming bookings: ", bookings);
        return ResponseEntity.ok(jsonAns);
    }
}
