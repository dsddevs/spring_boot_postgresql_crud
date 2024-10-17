package dsd.springboot_crud_postgresql.service;

import dsd.springboot_crud_postgresql.api.response.ApiResponse;
import dsd.springboot_crud_postgresql.dto.UserRegistrationDto;
import dsd.springboot_crud_postgresql.dto.UserUpdateDto;
import dsd.springboot_crud_postgresql.exceptions.UserNotFoundException;
import dsd.springboot_crud_postgresql.exceptions.UsernameExistsException;
import dsd.springboot_crud_postgresql.model.entity.User;
import dsd.springboot_crud_postgresql.model.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<User> registerUser(UserRegistrationDto registrationDto) {
        try {
            if (existsByUsername(registrationDto.getUsername())) {
                throw new UsernameExistsException("Error: Username already exists");
            }
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepo.save(user);
            return new ApiResponse<>(true, "Success: user registered", savedUser);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: user registration failed: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Page<User>> getUsers(Pageable pageable) {
        try {
            Page<User> users = userRepo.findAll(pageable);
            return new ApiResponse<>(true, "Success: users retrieved ", users);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: users not found: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<User> getUserById(Long id) throws UserNotFoundException {
        try {
            User userById = userRepo
                    .findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Error: User not found by id: " + id));
            return new ApiResponse<>(true, "Success: user founded by id: ", userById);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: user not found: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<User> getUserByUsername(String username) throws UsernameExistsException {
        try {
            User userByName = getByUsername(username);
            return new ApiResponse<>(true, "Success: user founded by username: ", userByName);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: user not found by username: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<User> updateUser(Long id, UserUpdateDto updateDto) throws UserNotFoundException {
        try {
            User user = getById(id);
            user.setUsername(updateDto.getUsername());
            user.setEmail(updateDto.getEmail());
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepo.save(user);
            return new ApiResponse<>(true, "Success: user updated: ", updatedUser);
        }catch (Exception e){
            return new ApiResponse<>(false, "Error: user not found by username: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> delete(Long id) throws UserNotFoundException {
        try {
            User user = getById(id);
            userRepo.delete(user);
            return new ApiResponse<>(true, "Success: user was deleted");
        }catch (Exception e){
            return new ApiResponse<>(false, "Error: user not deleted: " + e.getMessage());
        }
    }

    private boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    private User getById(Long id){
        return userRepo
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("Error: user not found by id: " + id));
    }

    private User getByUsername(String username){
        return userRepo
                .findByUsername(username)
                .orElseThrow(() -> new UsernameExistsException("Error: user already exists"));
    }

}
