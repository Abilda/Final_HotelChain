package com.example.springAuthenticationJWT.models;

import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {
    public Reservation() {};
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private Date checkin;
    private Date checkout;

    public Reservation(Date checkin, Date checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public Date getCheckout() {
        return checkout;
    }

    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guestId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotelId")
    private Hotel hotel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    private Room room;

}
