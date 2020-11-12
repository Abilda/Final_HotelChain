package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.*;
import com.example.springAuthenticationJWT.payload.request.BookingRequest;
import com.example.springAuthenticationJWT.payload.request.ReservationRequest;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.*;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;

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
    RoomTypeRepository roomTypeRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/get")
    public ResponseEntity<?> get(@PathParam("Id") Long Id) {
        return ResponseEntity.ok(reservationRepository.findById(Id).get());
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestHeader("Authorization") String authHeader, @RequestBody BookingRequest request) {
        String jwt = authHeader.substring(7, authHeader.length());
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        Optional<Hotel> hotel = hotelRepository.findById(request.getHotelId());
        Optional<Room> room = roomRepository.findById(request.getRoomId());
        Reservation reservation = new Reservation(request.getFrom(), request.getTo());
        reservation.setHotel(hotel.get());
        reservation.setRoomType(room.get().getRoomtype());
        reservation.setUser(user.get());
        reservationRepository.save(reservation);

        return ResponseEntity.ok(new MessageResponse("successfully booked"));
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestHeader("Authorization") String authHeader,
                                     @RequestBody ReservationRequest request) {
        String jwt = authHeader.substring(7, authHeader.length());
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        Reservation reservation = new Reservation(request.getFrom(), request.getTo());
        Optional<Hotel> hotel = hotelRepository.findById(request.getHotelId());
        Optional<RoomType> roomType = roomTypeRepository.findById(request.getRoomTypeId());
        reservation.setHotel(hotel.get());
        reservation.setRoomType(roomType.get());
        reservation.setUser(user.get());
        reservationRepository.save(reservation);
        return ResponseEntity.ok(new MessageResponse("successfully booked"));
    }

    @GetMapping("/availableRoomTypes")
    public ResponseEntity<?> availableRoomTypes(@RequestBody ReservationRequest request) {
        List<RoomType> availableRoomTypes = new ArrayList<>();
        List<RoomType> allRoomTypes = roomTypeRepository.findAll();
        for (RoomType roomType : allRoomTypes)
            if (nrofAvailableRoomType(request.getFrom(), request.getTo(), roomType.getId()) > 0)
                availableRoomTypes.add(roomType);
        return ResponseEntity.ok(availableRoomTypes);
    }

    public Integer nrofAvailableRoomType(Date from, Date to, Long roomTypeId) {
        Integer availableRooms = 0;
        List<Room> allRooms = roomRepository.findAll();
        for (Room room : allRooms)
            if (room.getRoomtype().getId() == roomTypeId)
                availableRooms++;
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations)
            if (reservation.getRoomType().getId() == roomTypeId) {
                boolean beginsInTheMiddleOfTheReservation = from.
                        compareTo(reservation.getCheckin()) >= 0 &&
                        reservation.getCheckout().compareTo(from) >= 0;
                boolean endsInTheMiddleOfTheReservation = to.compareTo(
                        reservation.getCheckin()) >= 0 && reservation.getCheckout().
                        compareTo(to) >= 0;
                boolean reservationInTheMiddle = reservation.getCheckin().compareTo(
                        from) >= 0 && to.compareTo(
                        reservation.getCheckout()) >= 0;
                if (beginsInTheMiddleOfTheReservation || endsInTheMiddleOfTheReservation ||
                        reservationInTheMiddle)
                    availableRooms--;
            }
        return availableRooms;
    }

    @PostMapping("/editBooking")
    public ResponseEntity<?> edit(@RequestHeader("authorization")String authHeader,
                                  @RequestBody ReservationRequest request) {
        String jwt = authHeader.substring(7, authHeader.length());
        Optional<Reservation> reservation = reservationRepository.findById(request.getReservationId());
        reservation.get().setCheckin(request.getFrom());
        reservation.get().setCheckout(request.getTo());
        reservationRepository.save(reservation.get());
        return ResponseEntity.ok(new MessageResponse("succesfully edited"));
    }

//    @GetMapping("/nrOfAvailableRooms")
//    public ResponseEntity<?> numberOfAvailableRooms(@RequestBody ReservationRequest request) {
//        int availableRooms = 0;
//        List<Room> allRooms = roomRepository.findAll();
//        for (Room room : allRooms)
//            if (room.getRoomtype().getId() == request.getRoomTypeId())
//                availableRooms++;
//        List<Reservation> reservations = reservationRepository.findAll();
//        for (Reservation reservation : reservations)
//            if (reservation.getRoomType().getId() == request.getRoomTypeId()) {
//                boolean beginsInTheMiddleOfTheReservation = request.getFrom().
//                        compareTo(reservation.getCheckin()) >= 0 &&
//                        reservation.getCheckout().compareTo(request.getFrom()) >= 0;
//                boolean endsInTheMiddleOfTheReservation = request.getTo().compareTo(
//                        reservation.getCheckin()) >= 0 && reservation.getCheckout().
//                        compareTo(request.getTo()) >= 0;
//                boolean reservationInTheMiddle = reservation.getCheckin().compareTo(
//                        request.getFrom()) >= 0 && request.getTo().compareTo(
//                        reservation.getCheckout()) >= 0;
//                if (beginsInTheMiddleOfTheReservation || endsInTheMiddleOfTheReservation ||
//                    reservationInTheMiddle)
//                    availableRooms--;
//            }
//        return ResponseEntity.ok(availableRooms);
//    }

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
