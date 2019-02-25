package com.interview.task.controller;

import com.interview.task.dto.UserDto;
import com.interview.task.dto.WalletDto;
import com.interview.task.exceptions.WalletCreationLimitException;
import com.interview.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Class represents user controller.
 */
@RestController
@PreAuthorize("hasAnyAuthority('USER')")
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService clientService) {
        this.userService = clientService;
    }

    /**
     * Method performs search for certain user.
     *
     * @param userId user id.
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") final Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    /**
     * Method returns registered all users.
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    /**
     * Method performs user update operation.
     *
     * @param userDto userDto
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody final UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateUser(userDto));
    }

    /**
     * Method performs delete user operation.
     *
     * @param userId user id.
     * @return ResponseEntity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Long userId) {
        userService.removeUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method get all user wallets.
     *
     * @param userId user id.
     * @return ResponseEntity
     */
    @GetMapping("/{id}/wallets")
    public ResponseEntity<?> getUserWallets(@PathVariable("id") final Long userId) {
        return ResponseEntity.ok().body(userService.getAllUserWallets(userId));
    }

    /**
     * Method creates new wallet for certain user.
     *
     * @param userId user id.
     * @param walletDto new wallet
     * @return ResponseEntity
     * @throws WalletCreationLimitException if user tries to create more than 3 wallets.
     */
    @PutMapping("/{id}/wallet/new")
    public ResponseEntity<?> createNewUserWallet(@PathVariable("id") final Long userId,
                                                 @RequestBody final WalletDto walletDto) throws WalletCreationLimitException {
        WalletDto createdWallet = userService.addWallet(userId, walletDto);
        return ResponseEntity.ok().body(createdWallet);
    }
}
