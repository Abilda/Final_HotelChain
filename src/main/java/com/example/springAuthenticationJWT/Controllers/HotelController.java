package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.Hotel;
import com.example.springAuthenticationJWT.models.RoomType;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/hotel")
public class HotelController {
    @Autowired
    HotelRepository hotelRepository;

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

}
