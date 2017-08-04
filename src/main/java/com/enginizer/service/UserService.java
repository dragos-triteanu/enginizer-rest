package com.enginizer.service;

import com.enginizer.model.enums.Role;
import com.enginizer.model.dto.CreateUserDTO;
import com.enginizer.model.dto.UserDTO;
import com.enginizer.model.entities.User;
import com.enginizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for executing operations of the {@link User} model object..
 */
@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User findUserByEmailAddress(final String mail) {

        return userRepository.findByEmail(mail);
    }

    public List<UserDTO> getUsers(){
        List<User> users=  userRepository.findAll();
        List<UserDTO> usersDto = new ArrayList<>();

        for (User u:users) {
            UserDTO user = convertUserToDto(u);
            usersDto.add(user);
        }

        return usersDto;
    }

    public void createAccount(CreateUserDTO user){
        userRepository.save(fromCreateUSerDTO(user));
    }

    public void updateUserPassword(User user){
        userRepository.updateUserPassword(user.getId(),user.getPassword());
    }


    private User fromCreateUSerDTO(CreateUserDTO dto){
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(true);
        user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        user.setRole(Role.ADMIN);
        return user;
    }

    private UserDTO convertUserToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUserName(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        return userDTO;
    }
}
