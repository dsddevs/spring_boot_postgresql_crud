package dsd.springboot_crud_postgresql.api.controller;

import dsd.springboot_crud_postgresql.api.response.ApiResponse;
import dsd.springboot_crud_postgresql.dto.UserRegistrationDto;
import dsd.springboot_crud_postgresql.dto.UserUpdateDto;
import dsd.springboot_crud_postgresql.model.entity.User;
import dsd.springboot_crud_postgresql.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Tag(name = "User Management", description = "APIs for user management")
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final IUserService iUserService;

    @Operation(summary = "Get connection status")
    @GetMapping("/status")
    public ResponseEntity<String> getStatusConnection() {
        try {
            return ResponseEntity.ok("Application connected");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Application connection failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Register a new user")
    @PostMapping()
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        ApiResponse<User> userRegistration = iUserService.registerUser(registrationDto);
        var response = userRegistration.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(response).body(userRegistration);
    }

    @Operation(summary = "Get all users")
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(Pageable pageable) {
        ApiResponse<Page<User>> response = iUserService.getUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        ApiResponse<User> response = iUserService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get a user by username")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        ApiResponse<User> response = iUserService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        ApiResponse<User> response = iUserService.updateUser(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        ApiResponse<Void> response = iUserService.delete(id);
        return ResponseEntity.ok(response);
    }

}
