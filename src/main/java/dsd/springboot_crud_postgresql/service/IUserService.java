package dsd.springboot_crud_postgresql.service;

import dsd.springboot_crud_postgresql.api.response.ApiResponse;
import dsd.springboot_crud_postgresql.dto.UserRegistrationDto;
import dsd.springboot_crud_postgresql.dto.UserUpdateDto;
import dsd.springboot_crud_postgresql.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    ApiResponse<User> registerUser(UserRegistrationDto registrationDto);
    ApiResponse<Page<User>> getUsers(Pageable pageable);
    ApiResponse<User> getUserById(Long id);
    ApiResponse<User> getUserByUsername(String username);
    ApiResponse<User> updateUser(Long id, UserUpdateDto updateDto);
    ApiResponse<Void> delete(Long id);
}
