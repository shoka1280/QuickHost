package com.Project.QuickHost.Service.impl;

import com.Project.QuickHost.Dto.ProfileUpdateRequestDto;
import com.Project.QuickHost.Dto.UserDto;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Repository.UserRepo;
import com.Project.QuickHost.Service.BookingService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.Project.QuickHost.Util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
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
            user.setDOB(profileUpdate.getDateOfBirth());}

        if(profileUpdate.getGender()!=null){
        user.setGender(profileUpdate.getGender());
        }
        if(profileUpdate.getPhonenumber()!=null)
        {
            user.setPhonenumber(profileUpdate.getPhonenumber());
        }


        userRepo.save(user);
    }


    public UserDto getUserProfile() {
        User user=getCurrentUser();
        log.info("Fetching profile for user: {}", user.getEmail());
        return modelMapper.map(user, UserDto.class);
    }
}
