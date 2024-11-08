package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.UserRequestDTO;
import com.swpgavf.back.dto.UserResponseDTO;
import com.swpgavf.back.entity.User;
import com.swpgavf.back.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    private final ObjectMapper objectMapper;

    public UserService(IUserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    // Authenticate method to check if username and password match
    @Override
    public User authenticate(String username, String password) {
        // Get the user by username from the repository (assuming you have a User entity and repository)
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;  // If password matches, return the user
        } else {
            return null;  // Return null if credentials are invalid
        }
    }


    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = mapToUser(userRequestDTO);
        userRepository.save(user);
        return mapToDTO(user);
    }

    private User mapToUser(UserRequestDTO userRequestDTO) {
        return objectMapper.convertValue(userRequestDTO, User.class);
    }

    private UserResponseDTO mapToDTO(User user) {
        return objectMapper.convertValue(user, UserResponseDTO.class);
    }

}
