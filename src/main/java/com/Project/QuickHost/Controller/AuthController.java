package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.UserDto;
import com.Project.QuickHost.Security.AuthService;
import com.Project.QuickHost.Security.Dto.LoginDto;
import com.Project.QuickHost.Security.Dto.LoginResponseDto;
import com.Project.QuickHost.Security.Dto.SignUpRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //For signUo
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequestDto signUpReq)
    {
        UserDto userDto=authService.SignUp(signUpReq);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
       String[] loginData =authService.login(loginDto);
       //refresh token need to be paresed inside cookies
        Cookie cookie=new Cookie("RefreshToken",loginData[1]);//refresh tooken
        cookie.setHttpOnly(true);//HttpOnly flag prevents client-side scripts from accessing the cookie, enhancing security against XSS attacks.
        cookie.setPath("/");//Sets the path for which the cookie is valid. In this case, it is set to the root path ("/"), meaning the cookie will be sent with requests to all paths of the application.
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDto(loginData[0]));


    }
//    @GetMapping("/refresh")
//    public ResponseEntity<String> generateAcccessToken(HttpServletRequest request)
//    {
//        String refreshToken= Arrays
//                .stream(request.getCookies())
//                .filter(cookie->"refreshToken".equals(cookie.getName()))
//                .findFirst()
//                .map(cookie->cookie.getValue())
//                .orElseThrow(()->new AuthenticationServiceException("Refresh token not found in cookies"));
//        retrurn new ResponseEntity<>(authrefreshToken)
//    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshingToken(HttpServletRequest request){
        String refeshToken=Arrays.stream(request.getCookies()).
                filter(cookie -> "RefreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("Refrresh token not found in cookie "));
        String accessToken=authService.refresh(refeshToken);
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }
}