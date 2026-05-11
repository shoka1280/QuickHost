package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    // Additional query methods can be defined here if needed
    void deleteByRoom(Room room);
   //Invetory reserve
   @Modifying
   @Query("""
   UPDATE Inventory  i
   SET i.reservedCount = i.reservedCount + :numberOfRooms
   WHERE i.room.id= :roomId
   AND i.date BETWEEN :startDate AND :endDate
   AND (i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
   AND i.closed=false
""")
   void intitBooking(@Param("roomId")Long roomId,
                         @Param("startDate")LocalDate startDate,
                         @Param("endDate")LocalDate endDate,
                         @Param("numberOfRooms")Integer numberOfRooms
   );


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
    @Query("""
                SELECT i
                FROM Inventory i
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                              AND i.closed = false
                  AND (i.totalCount - i.bookedCount) >= :numberOfRooms
                  
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)//TO AVOID LAST CONDITION
    List<Inventory> findAndLockReservedInventory(@Param("roomId") Long roomId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("numberOfRooms") Integer numberOfRooms);
    //get all the inventory and then putting the lock where room count availble from start to end date
    @Query("""
        SELECT i FROM Inventory i
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.reservedCount-i.bookedCount ) >= :roomsCount
          AND i.closed = false
""")
   @Lock(LockModeType.PESSIMISTIC_WRITE)//PUTTING LOCK ON AVAILBE INVENTORY
    List<Inventory> findAndLockAvailableInventory(
        @Param("roomId")Long roomId,
        @Param("startDate")LocalDate startDate,
        @Param("endDate")LocalDate endDate,
        @Param("roomsCount")Integer roomsCount
    );

    //COnfirm booking
    @Modifying
    @Query("""
UPDATE Inventory i
SET i.reservedCount=i.reservedCount - :numberOfRooms,
    i.bookedCount=i.bookedCount + :numberOfRooms
    
    where i.room.id= :roomId
    AND i.date BETWEEN :startDate AND :endDate
    AND (i.totalCount  - i.bookedCount) >= :numberOfRooms
    AND i.reservedCount>= :numberOfRooms
    AND i.closed=false
""")
    void confirmBooking(@Param("roomId")Long roomId,
                        @Param("startDate")LocalDate startDate,
                        @Param("endDate")LocalDate endDate,
                        @Param("numberOfRooms")Integer numberOfRooms//kitne hame room book krne hai
                        );



    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate dateAfter, LocalDate dateBefore);

    @Modifying
    @Query("""
    UPDATE Inventory i
    SET i.bookedCount= i.bookedCount - :numberOfRooms
     where i.room.id= :roomId
    AND i.date BETWEEN :startDate AND :endDate
    AND (i.totalCount  - i.bookedCount) >= :numberOfRooms
  
    AND i.closed=false
"""
    )
    void cancelBooking(@Param("roomId")Long roomId,
                        @Param("startDate")LocalDate startDate,
                        @Param("endDate")LocalDate endDate,
                        @Param("numberOfRooms")Integer numberOfRooms//kitne hame room book krne hai
    );

    List<Inventory> findByRoomOrderByDate(Room room);
    @Modifying
    @Query("""
      UPDATE  Inventory i 
            SET i.closed=:status,
             i.surgeFactor=:surgeFactor,
             i.price=:price
                   WHERE i.room.id=:roomId
                         AND i.date between :startDate AND :endDate 
                               """)
    void updateInventoryOnRequest(@Param("status")boolean status,
                                  @Param("surgeFactor") BigDecimal surgeFactor,
                                  @Param("price")BigDecimal price,
                                  @Param("roomId")Long roomId,
                                  @Param("startDate")LocalDate startDate,
                                    @Param("endDate")LocalDate endDate);
    @Query("""
                SELECT i
                FROM Inventory i
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                              AND i.closed = false
                
                  
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)//TO AVOID LAST CONDITION
   List<Inventory> findAndLockInventoryFrUpdation(@Param("roomId") Long roomId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate
                                     );


}
