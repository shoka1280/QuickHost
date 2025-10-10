package com.Project.QuickHost.Security;

import com.Project.QuickHost.Dto.UserDto;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Entity.enums.Roles;
import com.Project.QuickHost.Repository.UserRepo;
import com.Project.QuickHost.Security.Dto.LoginDto;
import com.Project.QuickHost.Security.Dto.SignUpRequestDto;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private  final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JWTservice jwtService;

    //login
    @Transactional
    public UserDto SignUp(@Valid SignUpRequestDto signUp)
    {
        Optional<User> user=userRepo.findByEmail(signUp.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User already exists"+user.get().getEmail());//.get() is to retive from optional
        }
        User newUser=modelMapper.map(signUp,User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(Set.of(Roles.GUEST));//Default guest role
        User savedUser=userRepo.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }
    //LOGIN,AUTHENTICATE USING AUTHENTICATION MANAGER(USING DAO AUTHENTICATION PROVIDER)
    @Transactional
    public String[] login(LoginDto login)
    {
        Authentication auth=authManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword()));//autehnticate method will authenticate user and
        User user=(User)auth.getPrincipal();//check
        String[]arr=new String[2];
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);
        arr[0]=(accessToken);arr[1]=refreshToken;
        return arr;
    }

    public String refresh(String reshtoken)
    {
        //return new access token
        Long id=jwtService.getUserIdFromToken(reshtoken);
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }

}
