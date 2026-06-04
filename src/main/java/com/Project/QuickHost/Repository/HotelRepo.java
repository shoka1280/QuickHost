package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Queue;


@Repository
public interface HotelRepo extends JpaRepository<Hotel,Long> {
    List<Hotel> findHotelByOwner(User user);
    List<Hotel> findAll();



}
