package com.example.springAuthenticationJWT.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private double seasonalRate;

    private Date startDate;

    private Date endDate;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "hotel_seasons",
            joinColumns = @JoinColumn(name = "season_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id"))
    private Set<Hotel> hotels = new HashSet<>();

    public Season() {}

    public Season(Date startDate, Date endDate, double seasonalRate) {
        this.seasonalRate = seasonalRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public double getSeasonalRate() {
        return seasonalRate;
    }

    public void setSeasonalRate(double seasonalRate) {
        this.seasonalRate = seasonalRate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(Set<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    public void clearHotels() {
        hotels.clear();
    }
}
