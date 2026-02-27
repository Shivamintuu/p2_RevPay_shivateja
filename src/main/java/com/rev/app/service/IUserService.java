package com.rev.app.service;

import com.rev.app.dto.UserDTO;
import java.util.List;

public interface IUserService {
    UserDTO registerUser(UserDTO userDTO, String password);
    UserDTO registerBusinessUser(UserDTO userDTO, String password);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    boolean verifyPassword(Long userId, String currentPassword);
    void updatePassword(Long userId, String currentPassword, String newPassword);
    void updateTransactionPin(Long userId, String newPin);
}
