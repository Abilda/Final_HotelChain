package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.*;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.SeasonRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/season/")
public class SeasonController {
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/getSeasons")
    public ResponseEntity<?> get() {
        HashMap<String, Set<String>> season_hotels = new HashMap<>();
        List<Season> allSeasons = seasonRepository.findAll();
        for (Season season : allSeasons)
            if (season.getHotels().size() > 0) {
                String seasonInfo = "From " + season.getStartDate() + " to " + season.getEndDate() + ", discount rate - " + season.getSeasonalRate();
                Set<String> hotelsInfo = new HashSet<>();
                Set<Hotel> hotels = season.getHotels();
                for (Hotel hotel : hotels)
                    hotelsInfo.add(hotel.getName() + ", at city " + hotel.getCity());
                season_hotels.put(seasonInfo, hotelsInfo);
            }
        return ResponseEntity.ok(season_hotels);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("authorization") String authHeader,
                                    @RequestBody Season season) {
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        boolean isAdmin = false;
        for (Role role : user.get().getRoles())
            if (role.getName() == ERole.ROLE_ADMIN) {
                isAdmin = true;
                break;
            }
        if (!isValidJWT || !isAdmin) {
            return ResponseEntity.ok("You are not authorized to access Admin's area");
        }
        Season createdseason = new Season(season.getStartDate(), season.getEndDate(), season.getSeasonalRate());
        seasonRepository.save(createdseason);
        System.out.print(createdseason.getId());
        return ResponseEntity.ok(new MessageResponse("succesfully created"));
    }

    @PostMapping("/addSeasonToHotel")
    public ResponseEntity<?> add(@RequestHeader("authorization") String authHeader,
                                 @PathParam("hotelId") Long hotelId, @PathParam("seasonId") Long seasonId) {
        String jwt = authHeader.substring(7, authHeader.length());
        boolean isValidJWT = jwtUtils.validateJwtToken(jwt);
        Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
        boolean isAdmin = false;
        for (Role role : user.get().getRoles())
            if (role.getName() == ERole.ROLE_ADMIN) {
                isAdmin = true;
                break;
            }
        if (!isValidJWT || !isAdmin) {
            return ResponseEntity.ok("You are not authorized to access Admin's area");
        }
        Optional<Season> season = seasonRepository.findById(seasonId);
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        season.get().addHotel(hotel.get());
        seasonRepository.save(season.get());
        return ResponseEntity.ok(new MessageResponse("Hotel was added to season"));
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> Delete(@RequestHeader("authorization") String authHeader,
                                    @RequestBody Object obj) {

        Long id = ((LinkedHashMap<String, Integer>)obj).get("seasonId").longValue();
        Optional<Season> season = seasonRepository.findById(id);
        season.get().clearHotels();
        seasonRepository.save(season.get());
        return ResponseEntity.ok(new MessageResponse("The season was successfully canceled"));
    }

    @GetMapping("/getadvisory")
    public ResponseEntity<?> getadvisory() {
        ArrayList<Season> activeSeasons = getCurrentSeasons();
        if (activeSeasons.size() == 0)
            return ResponseEntity.ok(new MessageResponse("Currently there is no any seasonal discounts." +
                    "However, we surely will have one soon. Stay tuned :)"));
        Season bestOffer = new Season();
        Double bestDiscount = 0.0;
        for (Season season : activeSeasons)
            if (season.getSeasonalRate() > bestDiscount) {
                bestDiscount = season.getSeasonalRate();
                bestOffer = season;
            }
        Set<Hotel> hotels = bestOffer.getHotels();
        StringBuilder discountHotels = new StringBuilder("");
        int num = 1;
        for (Hotel hotel : hotels) {
            discountHotels.append(hotel.getName() + " - " + hotel.getCity() + " city");
            if (num != hotels.size())
                discountHotels.append(", ");
            num++;
        }
        String advisory = "There is currently a " + bestDiscount + " % discount for rooms at " +
                discountHotels.toString();
        return ResponseEntity.ok(new MessageResponse(advisory));
    }

    private ArrayList<Season> getCurrentSeasons() {
        Date currentDate = new Date();
        List<Season> allSeasons = seasonRepository.findAll();
        ArrayList<Season> activeSeasons = new ArrayList<>();
        for (Season season : allSeasons)
            if (currentDate.compareTo(season.getStartDate()) >= 0 &&
                    season.getEndDate().compareTo(currentDate) >= 0)
                activeSeasons.add(season);
        return activeSeasons;
    }
}
