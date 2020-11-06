package com.example.springAuthenticationJWT.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Rooms")
public class Room {
    @Id
    private Long Room_number;
    private Integer floor;
    private boolean isClean;
    private boolean isEmpty;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomTypeId")
    private RoomType roomtype;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guestId")
    private User user;

    public boolean isClean() {
        return isClean;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public RoomType getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(RoomType roomtype) {
        this.roomtype = roomtype;
    }

    public Room() {}

    public Long getRoom_number() {
        return this.Room_number;
    }

    public void setId(Long Room_number) {
        this.Room_number = Room_number;
    }

    public Integer getFloor() {
        return this.floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setIsClean(Boolean isClean) {
        this.isClean = isClean;
    }

    public void setIsEmpty(Boolean isEmpty) {
        this.isEmpty = isEmpty;
    }


}
