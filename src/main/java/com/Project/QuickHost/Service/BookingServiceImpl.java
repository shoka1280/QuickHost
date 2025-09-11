package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import com.Project.QuickHost.Entity.*;
import com.Project.QuickHost.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Service
@Slf4j//simple logging fasad for java

@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepo bookingRepo;
    private final HotelRepo hotelRepo;
    private final RoomRepo roomRepo;
    private final InventoryRepo invRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final GuestRepo guestRepo;
    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest book) {
        //reserve the room for 10 mins[so that no other person can book in b/w hold price for 10min also
        //this can solved for

        Hotel hotel=hotelRepo.findById(book.getHotelId()).orElseThrow(()->new RuntimeException("room not available with id: "+book.getHotelId()));
        Room room=roomRepo.findById(book.getRoomId()).orElseThrow(()->new RuntimeException("room not available with id "+book.getRoomId()));

        log.info("Creating new booking for hotel: {} and room: {} from {} to {}", hotel.getName(), book.getRoomId(),book.getCheckInDate(),book.getCheckOutDate());

        //now get Inventory for cheking and checkOut data
        //also lock the inventory
        List<Inventory> inventoryList
                =invRepo.findAndLockAvailableInventory(room.getId(),
                book.getCheckInDate(),book.getCheckOutDate(),book.getRoomCount());
        //suppose i want to get for inventory but doest full fill criteria
        //if getting less inventory then

        long dayscount= ChronoUnit.DAYS.between(book.getCheckInDate(),book.getCheckOutDate())+1;
        if(inventoryList.size()!=dayscount)
        {
            throw new IllegalStateException("Room not available anymore");


        }

        //reserve the rooms/update booked count inventory
        for(Inventory inventory:inventoryList)
        {
            inventory.setReservedCount(inventory.getReservedCount()+book.getRoomCount());//current book count+room requested
            //save all for iterable entity
        }

        invRepo.saveAll(inventoryList);
        //in memorry user ,dummy user
        User user=new User();
        user.setId(1L);
//        userRepo.save(user);
        //TODO:calculate dyanamic price

        //createbooking
        Bookings bookings=Bookings.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .room(room)
                .hotel(hotel)
                .checkInDate(book.getCheckInDate())
                .checkOutDate(book.getCheckOutDate())
                .user(user)
                .roomCount(book.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();

        bookingRepo.save(bookings);
        return modelMapper.map(bookings,BookingDto.class);

    }
  public User getCurrentUser(){
        User user=new User();
        user.setId(1L);
        return user;
  }

    @Override
    public BookingDto addGuest(Long bookingId,List<GuestDto> guestList) {
       Bookings book=bookingRepo.findById(bookingId).orElseThrow(()->new RuntimeException("room not available with id "+bookingId));

        log.info(" Adding guest for booking id {}",bookingId);
        if(hasBookingExpired(book))
        {
            throw new IllegalStateException("booking expired");
        }
        if(book.getBookingStatus()!=BookingStatus.RESERVED)
        {
            throw  new IllegalStateException("Booking is not under reserved state,cant add guedt");
        }
        for(GuestDto guest1:guestList){
            Guest guest=modelMapper.map(guest1,Guest.class);
            guest.setUser(getCurrentUser());
           guest=guestRepo.save(guest);
           book.getGuests().add(guest); //why cant we use setGuest(becuase multiple guest are added)

        }
        book.setBookingStatus(BookingStatus.GUEST_ADDED);
        book=bookingRepo.save(book);
        return modelMapper.map(book,BookingDto.class);
    }
    public boolean hasBookingExpired(Bookings book)
    {
        return book.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

}
