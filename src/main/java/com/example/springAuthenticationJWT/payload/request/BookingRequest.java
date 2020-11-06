package com.example.springAuthenticationJWT.payload.request;

import java.util.Date;

public class BookingRequest {
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

    public BookingRequest() {};

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