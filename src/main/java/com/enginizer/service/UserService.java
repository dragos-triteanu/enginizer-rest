package com.enginizer.service;

import com.enginizer.model.dto.UserDTO;
import com.enginizer.model.entities.User;
import com.enginizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for executing operations of the {@link User} model object..
 */
@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO findUserDtoByEmailAddress(final String mail) {
        User user = userRepository.findByMail(mail);
        return convertUserToDto(user);
    }

    public User findUserByEmailAddress(final String mail) {
        return userRepository.findByMail(mail);
    }


    public User createAccount(User user){
        return userRepository.saveAndFlush(user);
    }

    public void updateUserInfo(User user){
        userRepository.updateUserInfo(user.getId(),user.getFirstName(),user.getLastName(),user.isNotificationsEnable());
    }

    private UserDTO convertUserToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUserName(user.getMail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setNotificationsEnable(user.isNotificationsEnable());

        return userDTO;
    }
}
