package com.swpgavf.back.service;

import com.swpgavf.back.dto.UserRequestDTO;
import com.swpgavf.back.dto.UserResponseDTO;
import com.swpgavf.back.entity.User;

public interface IUserService {
    User authenticate(String username, String password);

    UserResponseDTO create( UserRequestDTO userRequestDTO);
}
