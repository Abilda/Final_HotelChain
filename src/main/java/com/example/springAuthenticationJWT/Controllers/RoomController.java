package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.Hotel;
import com.example.springAuthenticationJWT.models.Reservation;
import com.example.springAuthenticationJWT.models.Room;
import com.example.springAuthenticationJWT.models.User;
import com.example.springAuthenticationJWT.payload.response.JwtResponse;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.*;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import com.example.springAuthenticationJWT.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;

    @GetMapping("/roomTypeInfo")
    public ResponseEntity<?> info(@PathParam("roomTypeId") int roomTypeId) {
        List<Room> allRooms = roomRepository.findAll();
        ArrayList<Room> roomTypeRooms = new ArrayList<Room>();
        for (Room room : allRooms) {
            if (room.getRoomtype().getId() == roomTypeId)
                roomTypeRooms.add(room);
        }
        return ResponseEntity.ok(roomTypeRooms);
    }

//why do i need you?
    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestHeader("Authorization") String authHeader,
                                  @RequestBody RequestHelper request) {
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        if (!isValidJWT)
            return ResponseEntity.ok("Autorization error :(. Pls try to (re)login");
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


}

class RequestHelper {
    private Date from;
    private Date to;
    private Long hotelId;
    private Long roomId;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public RequestHelper() {};

    public Date getFrom() {
        return from;
    }
    public Date getTo() {
        return to;
    }

    public void setFrom(Date from) {
        this.from = from;
    }
    public void setTo(Date to) {
        this.to = to;
    }
};