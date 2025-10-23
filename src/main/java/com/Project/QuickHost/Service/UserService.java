package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.ProfileUpdateRequestDto;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Repository.UserRepo;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.Project.QuickHost.Util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final BookingService bookService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findByEmail(username)
                .orElseThrow(()->new RuntimeException("User not found"));
    }

    public User getUserById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public void profileUpdate(ProfileUpdateRequestDto profileUpdate) {
        User user=getCurrentUser();
        if(profileUpdate.getName()!=null){
        user.setName(profileUpdate.getName());}
        if(profileUpdate.getDateOfBirth()!=null){
            user.setDOB(profileUpdate.getDOB());}

        if(profileUpdate.getGender()!=null){
        user.setGender(profileUpdate.getGender());
        }


        userRepo.save(user);
    }

    public List<BookingDto> getMyBookings() {
        User user= getCurrentUser();
        List<BookingDto>list=bookService.getAllBookingsByUser(user);
        return list;


    }
}
