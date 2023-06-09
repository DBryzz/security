package org.csbf.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csbf.security.service.UserService;
import org.csbf.security.utils.helperclasses.HelperDto;
import org.csbf.security.utils.helperclasses.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/secure")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/user/update", consumes = { "multipart/form-data" }, produces = { "application/json" })
    @Operation(summary = "Edit User Profile", description = "Modify currently authenticated user's profile information", tags = { "user" })
    public ResponseEntity<ResponseMessage> updateAuthUserProfile( @RequestParam("image") Optional<MultipartFile> file, @RequestParam("jsonData") String jsonData) {
        return new ResponseEntity<>(userService.updateAuthUserProfile(file, jsonData), HttpStatus.CREATED);
    }

    @PostMapping(value = "/admin/update-user/{userId}", consumes = { "multipart/form-data" }, produces = { "application/json" })
    @Operation(summary = "Edit User Profile", description = "Modify user's profile information using his id", tags = { "admin" })
    public ResponseEntity<ResponseMessage> updateUserProfile(@PathVariable(name = "userId") UUID userId, @RequestParam("image") Optional<MultipartFile> file, @RequestParam("jsonData") String jsonData) {
        return new ResponseEntity<>(userService.updateUserProfile(userId, file, jsonData), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAuthority('ADMIN') ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/admin/user/{userId}")
    @Operation(summary = "Get User Profile", description = "Get user's profile information using userId", tags = { "admin" })
    public HelperDto.UserFullDto getUserProfile(@PathVariable(name = "userId") UUID userId) { return userService.getUserProfile(userId); }

//    @PreAuthorize("hasAuthority('USER') ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/user/get")
    @Operation(summary = "Get User Profile", description = "Get currently authenticated user's information", tags = { "user" })
    public HelperDto.UserDto getAuthUserProfile() {
        log.info("TEST: In Controller");

        return userService.getAuthUserProfile();
    }

    @GetMapping("/admin/user-pix/{userId}")
    @Operation(summary = "Get User's Profile Image", description = "Get user's profile image using userId", tags = { "admin" })
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable(name = "userId") UUID userId) {
        return new ResponseEntity<>(userService.getUserProfilePicture(userId), HttpStatus.OK);
    }

    @GetMapping("/user/pix")
    @Operation(summary = "Get User's Profile Image", description = "Get currently auth user's profile image", tags = { "user" })
    public ResponseEntity<byte[]> getProfileImage() {
        return new ResponseEntity<>(userService.getProfilePicture(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/user-pix/{userId}/del")
    @Operation(summary = "Delete User's Profile Image", description = "Delete user's profile image using userId", tags = { "admin" })
    public ResponseEntity<ResponseMessage> deleteProfileImage(@PathVariable(name = "userId") UUID userId) {
        userService.deleteUserProfilePic(userId);
        return new ResponseEntity<>(new ResponseMessage.SuccessResponseMessage("Image deleted"), HttpStatus.PARTIAL_CONTENT);
    }

    @DeleteMapping("/admin/del/user/{userId}")
    @Operation(summary = "Delete User's Profile Image", description = "Delete user using userId", tags = { "admin" })
    public ResponseEntity<ResponseMessage> hardDeleteProfile(@PathVariable(name = "userId") UUID userId) {
        userService.deleteUserProfile(userId);
        return new ResponseEntity<>(new ResponseMessage.SuccessResponseMessage("User deleted"), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/admin/soft-del/user/{userId}")
    @Operation(summary = "Soft delete User's Profile Image", description = "Soft delete user using userId", tags = { "admin" })
    public ResponseEntity<ResponseMessage> softDeleteProfile(@PathVariable(name = "userId") UUID userId) {
        return new ResponseEntity<>(userService.softDelete(userId), HttpStatus.PARTIAL_CONTENT);
    }
}
