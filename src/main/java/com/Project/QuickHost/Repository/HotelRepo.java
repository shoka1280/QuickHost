package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HotelRepo extends JpaRepository<Hotel,Long> {
//    List<Hotel> getAllHotel();

}
