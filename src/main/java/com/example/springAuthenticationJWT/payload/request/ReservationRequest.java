package com.example.springAuthenticationJWT.payload.request;

import java.util.Date;

public class ReservationRequest {
    private Date from;
    private Date to;
    private Long hotelId;
    private Long roomTypeId;

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    private Long reservationId;

    public ReservationRequest() {};

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }


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
}
