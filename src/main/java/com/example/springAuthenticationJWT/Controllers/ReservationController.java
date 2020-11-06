package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.Hotel;
import com.example.springAuthenticationJWT.models.Reservation;
import com.example.springAuthenticationJWT.models.Room;
import com.example.springAuthenticationJWT.models.User;
import com.example.springAuthenticationJWT.payload.request.BookingRequest;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.ReservationRepository;
import com.example.springAuthenticationJWT.repository.RoomRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/bookings")
public class ReservationController {
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

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestHeader("Authorization") String authHeader, @RequestBody BookingRequest request) {
        String jwt = authHeader.substring(7, authHeader.length());
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        Optional<Hotel> hotel = hotelRepository.findById(request.getHotelId());
        Optional<Room> room = roomRepository.findById(request.getRoomId());
        Reservation reservation = new Reservation(request.getFrom(), request.getTo());
        reservation.setHotel(hotel.get());
        reservation.setRoom(room.get());
        reservation.setUser(user.get());
        reservationRepository.save(reservation);

        return ResponseEntity.ok(new MessageResponse("successfully booked"));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authHeader, @RequestBody Object obj) {
        Long id = ((LinkedHashMap<String, Integer>)obj).get("Id").longValue();
        boolean isValidJWT = jwtUtils.validateJwtToken(authHeader.substring(7, authHeader.length()));
        if (!isValidJWT)
            return ResponseEntity.ok("Autorization error :(. Pls try to (re)login");
        try {
            Optional<Reservation> booking = reservationRepository.findById(id);
            reservationRepository.delete(booking.get());
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse("Could not delete, exception message: "
                    + e.getMessage() + ". Most probably you are trying to delete non-existing booking :)"));
        }
        return ResponseEntity.ok(new MessageResponse("The reservation with id - " + id +
                " was successfully deleted"));
    }

}
