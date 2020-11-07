package com.example.springAuthenticationJWT.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="seasons")
public class Season {
    @Id
    private Long Id;

    private double seasonalRate;

    private Date startDate;

    private Date endDate;

    @OneToMany(mappedBy = "season")
    private Set<Hotel> hotels;

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
}
