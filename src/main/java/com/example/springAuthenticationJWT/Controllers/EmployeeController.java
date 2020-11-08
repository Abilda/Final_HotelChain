package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.models.*;
import com.example.springAuthenticationJWT.payload.response.MessageResponse;
import com.example.springAuthenticationJWT.repository.EmployeeRepository;
import com.example.springAuthenticationJWT.repository.HotelRepository;
import com.example.springAuthenticationJWT.repository.ScheduleRepository;
import com.example.springAuthenticationJWT.repository.UserRepository;
import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees(@RequestHeader("authorization")String authHeader,
                                             @PathParam("hotelId") Long hotelId) {
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
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> hotelEmployees = new ArrayList<>();
        for (Employee employee : allEmployees)
            if (employee.getHotel().getId() == hotelId)
                hotelEmployees.add(employee);
        return ResponseEntity.ok(hotelEmployees);
    }

    @GetMapping("/getschedule")
    public ResponseEntity<?> getSchedule(@RequestHeader("authorization") String authHeader,
                                         @PathParam("employeeId") Long employeeId) {
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
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return ResponseEntity.ok(employee.get().getSchedules());
    }

    @PostMapping("adjustSchedule")
    public ResponseEntity<?> adjustHours(@RequestHeader("authorization") String authHeader,
                                         @RequestBody Schedule schedule) {
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
        Optional<Schedule> newSchedule = scheduleRepository.findById(schedule.getId());
        newSchedule.get().setDayOfTheWeek(schedule.getDayOfTheWeek());
        newSchedule.get().setEndTime(schedule.getEndTime());
        newSchedule.get().setStartTime(schedule.getStartTime());
        scheduleRepository.save(newSchedule.get());
        return ResponseEntity.ok(new MessageResponse("Schedule and working hours were successfully adjusted"));
    }
}
