package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.*;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.ReservationRepository;
import com.example.springAuthenticationJWT.repository.RoomRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.websocket.server.PathParam;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/hotel")
public class HotelController {
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

    @GetMapping
    public List<Hotel> index() {
        List<Hotel> allHotels = hotelRepository.findAll();
        return allHotels;
    }

    @GetMapping("/about")
    public ResponseEntity<?> about(@PathParam("Id") Long Id) {
        if (!hotelRepository.existsById(Id))
            return ResponseEntity.ok(new MessageResponse("Hotel with id " + Id + " does not exist"));
        Optional<Hotel> hotel = hotelRepository.findById(Id);
        Set<RoomType> roomTypesoftheHotel = hotel.get().getRoomTypes();
        System.out.print(hotel.get().getName());
        return ResponseEntity.ok(roomTypesoftheHotel);
    }

//    @GetMapping("/getGuests")
//    public ResponseEntity<?> getGuests(@RequestHeader("authorization") String authHeader) {
//        String jwt = authHeader.substring(7, authHeader.length());
//        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
//        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
//        boolean isModerator = false;
//        for (Role role : user.get().getRoles())
//            if (role.getName() == ERole.ROLE_MODERATOR) {
//                isModerator = true;
//                break;
//            }
//        if (!isValidJWT || !isModerator) {
//            return ResponseEntity.ok("You are not authorized to access Clerks area");
//        }
//        List<Reservation> allReservations = reservationRepository.findAll();
//        List<Reservation> activeReservations = new ArrayList<>();
//        HashMap<String, List<Long>> guestReservations = new HashMap<>();
////        guestReservations - username to active reservations Ids
//        Date current = new Date();
//        for (Reservation reservation : allReservations)
//            if (reservation.getCheckout().compareTo(current) > 0) {
//                if (!guestReservations.containsKey(reservation.getUser().getUsername()))
//                    guestReservations.put(reservation.getUser().getUsername(), new ArrayList<Long>());
//                guestReservations.get(reservation.getUser().getUsername()).add(reservation.getId());
//            }
//        return ResponseEntity.ok(guestReservations);
//    }
//
//    @GetMapping("getActiveReservationsForUser")
//    public ResponseEntity<?> getReservations(@RequestHeader("authorization") String authHeader, @PathParam("Id") Long Id){
//        String jwt = authHeader.substring(7, authHeader.length());
//        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
//        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
//        boolean isModerator = false;
//        for (Role role : user.get().getRoles())
//            if (role.getName() == ERole.ROLE_MODERATOR) {
//                isModerator = true;
//                break;
//            }
//        if (!isValidJWT || !isModerator) {
//            return ResponseEntity.ok("You are not authorized to access Clerks area");
//        }
//        List<Reservation> allReservations = reservationRepository.findAll();
//        List<Long> reservationsForUser = new ArrayList<>();
//        Date current = new Date();
//        System.out.println("UserId - " + Id);
//        for (Reservation reservation : allReservations)
//            if (reservation.getCheckout().compareTo(current) > 0 && reservation.getUser().getId() == Id)
//                reservationsForUser.add(reservation.getId());
//        return ResponseEntity.ok(reservationsForUser);
//    }

}
