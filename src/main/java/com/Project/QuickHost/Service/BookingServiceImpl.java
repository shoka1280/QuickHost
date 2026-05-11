package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import com.Project.QuickHost.Entity.*;
import com.Project.QuickHost.Entity.enums.BookingStatus;
import com.Project.QuickHost.Repository.*;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import com.Project.QuickHost.exception.UnAuthorisedException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.Project.QuickHost.Util.AppUtils.getCurrentUser;

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
    private final CheckoutService checkoutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;

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

//        reserve the rooms/update booked count inventory
//        for(Inventory inventory:inventoryList)
//        {
//            inventory.setReservedCount(inventory.getReservedCount()+book.getRoomCount());//current book count+room requested
//            //save all for iterable entity
//        }
        invRepo.intitBooking(room.getId(),book.getCheckInDate(),book.getCheckOutDate(),book.getRoomCount());
        invRepo.saveAll(inventoryList);
        //in memorry user ,dummy user

//        userRepo.save(user);
        //TODO:calculate dyanamic price
        BigDecimal currTotalAmt=pricingService.calcTotalPrice(inventoryList);
        BigDecimal totalAmt=currTotalAmt.multiply(BigDecimal.valueOf(book.getRoomCount()));

        //createbooking
        Bookings bookings=Bookings.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .room(room)
                .hotel(hotel)
                .checkInDate(book.getCheckInDate())
                .checkOutDate(book.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(book.getRoomCount())
                .amount(totalAmt)
                .build();

        bookingRepo.save(bookings);
        return modelMapper.map(bookings,BookingDto.class);

    }

    @Override
    @Transactional
    public String intiatePayements(Long bookingId) {
        Bookings booking=bookingRepo.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not bound with id "+bookingId));
        User user=getCurrentUser();//curent user from security context  holder
        //checking if user is owner or not of booking
        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorisedException("Unauthorized user access of ");
        }
        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has expired");
        }
       String sessionUrl= checkoutService.getCheckOutSession(booking,
                frontendUrl+"/payments/success",frontendUrl+"/payments/failure");
        booking.setBookingStatus(BookingStatus.PAYEMENT_PENDING);
        bookingRepo.save(booking);
        return sessionUrl; //when click weebhook will be used

    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        //check what event is it
        if("checkout.session.completed".equals(event.getType()))
        {
            Session session =(Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if(session==null){return;}

            String sessionId=session.getId();

             Bookings booking=
                     bookingRepo.findByStripePaymentsessionId(sessionId)
                             .orElseThrow(() -> new ResourceNotFoundException(String.format("Booking SessionId not found: %s", sessionId)));

             //Confirm booking
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            //aQUIRE LOCK
            invRepo.findAndLockReservedInventory(booking.getRoom().getId(),
                    booking.getCheckInDate(),booking.getCheckOutDate(),
                    booking.getRoomCount());

            invRepo.confirmBooking(booking.getRoom().getId(),
                    booking.getCheckInDate(),booking.getCheckOutDate(),
                    booking.getRoomCount());

            log.info("successfully confirmed booking for booking id: {}",booking.getId());
            //update the booked account in inventory

        }else{
            log.warn("Unhandled event type: {}",event.getType());
        }

    }

    @Override
    @Transactional
    public void initiateCancel(Long bookingId) {
        Bookings booking=bookingRepo.findById(bookingId).orElseThrow(()->new RuntimeException("room not available with id "+bookingId));

        User user=getCurrentUser();
        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorisedException("User not authroized to cancel this booking");
        }
        if(booking.getBookingStatus()!=BookingStatus.CONFIRMED)
        {
            throw new IllegalStateException("Only confirmed status be cancelled");
        }

        log.info(" Cancelling booking for booking id {}",bookingId);

        booking.setBookingStatus(BookingStatus.CANCELLED);
//        List<Guest> list=bookingRepo.findGuestsByBookingId(bookingId);
       bookingRepo.save(booking);
       //Updating inventory
        invRepo.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());
        invRepo.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(),
                booking.getRoomCount());

         //Refund
        try {
            Session session=Session.retrieve(booking.getStripePaymentsessionId());
            RefundCreateParams refundCreateParams= RefundCreateParams
                    .builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();
            Refund.create(refundCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public BookingStatus getBookingStatus(Long bookingId) {
        Bookings booking=bookingRepo.findById(bookingId).orElseThrow(()->new RuntimeException("room not available with id "+bookingId));

        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("User not authroized to add guest for this account");
        }
        return booking.getBookingStatus();
    }

    @Override
    public List<BookingDto> getAllBookingsByUser() {
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return bookingRepo.findByUser(user).stream()
                .map((book)->modelMapper.map(book,BookingDto.class))
                .collect(Collectors.toList());
    }



    @Override
    public BookingDto addGuest(Long bookingId,List<GuestDto> guestList) {
       Bookings book=bookingRepo.findById(bookingId).orElseThrow(()->new RuntimeException("room not available with id "+bookingId));

       //CHECK WHETER USER OWNS BOOKING
        User user=getCurrentUser();
        if(!user.equals(book.getUser())){
            throw new UnAuthorisedException("User not authroized to add guest for this account");
        }//equal and hascode based on id

        log.info(" Adding guest for booking id {}",bookingId);
        if(hasBookingExpired(book))
        {
            throw new IllegalStateException("booking expired");
        }
        if(book.getBookingStatus()!=BookingStatus.RESERVED)
        {
            throw  new IllegalStateException("Booking is not under reserved state,cant add guest");
        }
        for(GuestDto guest1:guestList){
            Guest guest=modelMapper.map(guest1,Guest.class);
            guest.setUser(user);
           guest=guestRepo.save(guest);
           book.getGuests().add(guest); //why cant we use setGuest(becuase multiple guest are added)

        }
        book.setBookingStatus(BookingStatus.GUEST_ADDED);
        book=bookingRepo.save(book);
        return modelMapper.map(book,BookingDto.class);
    }
    //Since we are holding user booking for 10 mintues we need to check whether booking as expired or not
    public boolean hasBookingExpired(Bookings book)
    {
        return book.getCreatedAt().plusMinutes(30).isBefore(LocalDateTime.now());
    }

    //getting authenticated user [checkking wheter current user and booking user are same or not]



}
