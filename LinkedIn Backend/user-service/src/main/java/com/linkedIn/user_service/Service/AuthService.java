package com.linkedIn.user_service.Service;

import com.linkedIn.user_service.Dto.LoginRequestDto;
import com.linkedIn.user_service.Dto.SignUpRequestDto;
import com.linkedIn.user_service.Dto.UserDto;
import com.linkedIn.user_service.Entity.User;
import com.linkedIn.user_service.Exceptions.BadRequestException;
import com.linkedIn.user_service.Exceptions.ResourceNotFoundException;
import com.linkedIn.user_service.Repository.UserRepository;
import com.linkedIn.user_service.Utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        User user=userRepository.findByEmail(signUpRequestDto.getEmail())
                .orElse(null);

        if(user!=null){
            throw new ResourceNotFoundException("User is already registred with this email :"+signUpRequestDto.getEmail());
        }

        User newUser= modelMapper.map(signUpRequestDto, User.class);
        newUser.setPassword(PasswordUtils.hashPassword(signUpRequestDto.getPassword()));

        User savedUser=userRepository.save(newUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user=userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User is not registred with this email :"+loginRequestDto.getEmail()));

        boolean passwordMatch=PasswordUtils.checkPassword(loginRequestDto.getPassword(),user.getPassword());

        if(!passwordMatch){
            throw new BadRequestException("Password is Incorrect.");
        }

        return jwtService.generateAccessToken(user);

    }
}
