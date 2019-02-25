package com.interview.task.service;

import com.interview.task.dto.UserDto;
import com.interview.task.dto.WalletDto;
import com.interview.task.entity.User;
import com.interview.task.entity.Wallet;
import com.interview.task.enums.Message;
import com.interview.task.enums.Role;
import com.interview.task.exceptions.UserNotFoundException;
import com.interview.task.exceptions.WalletCreationLimitException;
import com.interview.task.mapper.UserMapper;
import com.interview.task.mapper.WalletMapper;
import com.interview.task.repository.UserRepository;
import com.interview.task.repository.WalletRepository;
import com.interview.task.utils.WalletValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.interview.task.utils.WalletValidatorUtil.checkAvailableCurrencyForCreation;

/**
 * Class represents UserService implementation.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final WalletRepository walletRepository,
                           final PasswordEncoder passwordEncoder,
                           final UserMapper userMapper,
                           final WalletMapper walletMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
    }

    /**
     * Method searches and returns user by email.
     *
     * @param email user email
     * @return Optional<User>
     */
    @Override
    public Optional<User> getUserByEmail(final String email) {
        if (!isUserExistByEmail(email)) {
            LOG.error(Message.USER_NOT_FOUND.getMsgBody());
            throw new UserNotFoundException(Message.USER_NOT_FOUND.getMsgBody());
        }
        return userRepository.findByEmail(email);
    }

    /**
     * Method checks user presence in database by email.
     *
     * @param email user email
     * @return true if user is present, false otherwise
     */
    @Override
    public Boolean existsUserByEmail(final String email) {
        return isUserExistByEmail(email);
    }

    /**
     * Method checks user presence by email.
     *
     * @param email email
     * @return true if user exists, false otherwise
     */
    private Boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Method save new user to database.
     *
     * @param user new user
     * @return User
     */
    @Override
    public User saveNewUser(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        return userRepository.save(user);
    }

    /**
     * Method updates existed user.
     *
     * @param userDto userDto with updated info
     * @return UserDto
     */
    @Override
    public UserDto updateUser(final UserDto userDto) {
        if (!isUserExistByEmail(userDto.getEmail())) {
            LOG.error(Message.USER_NOT_FOUND.getMsgBody());
            throw new UserNotFoundException(Message.USER_NOT_FOUND.getMsgBody());
        }
        final User updated = userRepository.save(parseToUserEntity(userDto));
        return parseToUserDto(updated);
    }

    /**
     * Method returns list of existed users.
     *
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> getAllUsers() {
        final List<UserDto> userDtoList = new ArrayList<>();
        final List<User> users = userRepository.findAll();
        users.forEach(u -> userDtoList.add(
                new UserDto(
                        u.getUserId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getPassword(),
                        u.getRoles(),
                        u.getWallets())));
        return userDtoList;
    }

    /**
     * Method returns user by id.
     *
     * @param userId user id.
     * @return UserDto
     */
    @Override
    public UserDto getUserById(final Long userId) {
        checkUserExistence(userId);
        final User user = userRepository.getOne(userId);
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                user.getWallets()
        );
    }

    /**
     * Method allows user to addBalance new wallet.
     *
     * @param userId user id
     * @param walletDto new wallet
     * @return WalletDto
     * @throws WalletCreationLimitException can be thrown if user tries to create more then 3 wallets
     */
    @Transactional
    @Override
    public WalletDto addWallet(final Long userId, final WalletDto walletDto) throws WalletCreationLimitException {
        checkUserExistence(userId);
        final List<WalletDto> userWallets = userRepository.getAllClientWallets(userId);
        checkAvailableCurrencyForCreation(walletDto.getCurrency(), userWallets);
        final Optional<User> user = userRepository.findById(userId);
        if (userWallets.size() < 3) {
            Wallet savedWallet = walletRepository.save(parseToWalletEntity(walletDto));
            User currentUser = user.get();
            currentUser.getWallets().add(savedWallet);
            userRepository.save(currentUser);
            return parseToWalletDto(savedWallet);
        } else {
            LOG.error(Message.WALLET_CREATION_LIMIT.getMsgBody());
            throw new WalletCreationLimitException(Message.WALLET_CREATION_LIMIT.getMsgBody());
        }
    }

    /**
     * Method returns all wallets for a certain user.
     *
     * @param userId user id
     * @return List<WalletDto>
     */
    @Override
    public List<WalletDto> getAllUserWallets(final Long userId) {
        checkUserExistence(userId);
        return userRepository.getAllClientWallets(userId);
    }

    /**
     * Method removes user by id.
     *
     * @param userId user id.
     */
    @Override
    public void removeUser(final Long userId) {
        checkUserExistence(userId);
        userRepository.deleteById(userId);
    }

    /**
     * Method checks user presence in database.
     *
     * @param userId user id
     */
    private void checkUserExistence(final Long userId) {
        if (!userRepository.existsById(userId)) {
            LOG.error(Message.USER_NOT_FOUND.getMsgBody());
            throw new UserNotFoundException(Message.USER_NOT_FOUND.getMsgBody());
        }
    }

    /**
     * Method parse userDto to user entity.
     *
     * @param userDto userDto
     * @return User
     */
    private User parseToUserEntity(final UserDto userDto) {
        return userMapper.toEntity(userDto);
    }

    /**
     * Method parse user entity to UserDto.
     *
     * @param user user
     * @return UserDto
     */
    private UserDto parseToUserDto(final User user) {
        return userMapper.toDto(user);
    }

    /**
     * Method parse walletDto to wallet entity.
     *
     * @param walletDto walletDto
     * @return Wallet
     */
    private Wallet parseToWalletEntity(final WalletDto walletDto) {
        return walletMapper.toEntity(walletDto);
    }

    /**
     * Method parse wallet entity to walletDto.
     *
     * @param wallet wallet
     * @return WalletDto
     */
    private WalletDto parseToWalletDto(final Wallet wallet) {
        return walletMapper.toDto(wallet);
    }
}
