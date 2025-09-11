package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    // Additional query methods can be defined here if needed
    void deleteByRoom(Room room);


   @Query("""
      SELECT DISTINCT i.hotel FROM Inventory i WHERE i.city=:city  AND i.date BETWEEN :startDate AND :endDate
                  AND i.closed=false
                  AND (i.totalCount - i.bookedCount- i.reservedCount) >= :roomsCount
                        GROUP BY i.hotel,i.room
                              HAVING COUNT(i.date)=:dateCount""")//distict baes on hotel androom

    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city")String city,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("roomsCount")Integer roomsCount,
            @Param("dateCount")Integer dateCount,
            Pageable pageable
    );
    //get all the inventory and then putting the lock where room count availble from start to end date
    @Query("""
        SELECT i FROM Inventory i
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.reservedCount-i.bookedCount ) >= :roomsCount
          AND i.closed = false
""")
   @Lock(LockModeType.PESSIMISTIC_WRITE)//PUTTING LOCK
    List<Inventory> findAndLockAvailableInventory(
        @Param("roomId")Long roomId,
        @Param("startDate")LocalDate startDate,
        @Param("endDate")LocalDate endDate,
        @Param("roomsCount")Integer roomsCount
    );



    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate dateAfter, LocalDate dateBefore);
}
