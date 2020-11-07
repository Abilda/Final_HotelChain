package com.example.springAuthenticationJWT.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    private String city;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "hotel_roomTypes",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "room_type_id"))
    private Set<RoomType> roomTypes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seasonId")
    private Season season;

    public Hotel() {}

    public Hotel(String name, String city) {
        this.city = city;
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Set<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Set<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String city) {
        this.city = city;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return city;
    }
}
