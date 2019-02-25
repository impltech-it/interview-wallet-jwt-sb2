package com.interview.task.controller;

import com.interview.task.ApplicationRunner;
import com.interview.task.entity.User;
import com.interview.task.entity.Wallet;
import com.interview.task.enums.Currency;
import com.interview.task.repository.UserRepository;
import com.interview.task.utils.JsonParserUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ApplicationRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserControllerIntegrationTest {

    private static final String API_USER = "/api/user";

    private static final String UPDATE_USER_JSON = "json/update-user.json";
    private static final String NEW_WALLET_JSON = "json/new-wallet.json";
    private static final String NOT_FOUND_RESPONSE_JSON = "json/login-error-user-not-found-response.json";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenGetAllUsersWithAuthorizedUserThenReturnStatusIsOk() throws Exception {
        userRepository.save(new User("user", "user@gmail.com", passwordEncoder.encode("0plhrtff"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.EUR, 1000D));
                }}));
        userRepository.save(new User("user2", "user2@gmail.com", passwordEncoder.encode("234rvvcdf"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.UAH, 12000D));
                }}));
        mockMvc.perform(get(API_USER))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenGetCertainUserByIdWithAuthorizedUserThenReturnStatusIsOk() throws Exception {
        User user = userRepository.save(new User("user", "user@gmail.com", passwordEncoder.encode("0plhrtff"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.EUR, 1000D));
                }}));
        mockMvc.perform(get(API_USER + "/{id}", user.getUserId()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenUpdateNotExistedUserWithAuthorizedUserThenThrowException() throws Exception {
        String jsonSrc = JsonParserUtil.parseJsonToObject(UPDATE_USER_JSON).toString();
        String res = mockMvc.perform(put(API_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSrc))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expected = JsonParserUtil.parseJsonToObject(NOT_FOUND_RESPONSE_JSON).toString();
        JSONAssert.assertEquals(expected, res, JSONCompareMode.LENIENT);
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenUpdateUserWithAuthorizedUserThenReturnStatusIsOk() throws Exception {
        userRepository.save(new User("user", "user@gmail.com", passwordEncoder.encode("0plhrtff"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.EUR, 1000D));
                }}));
        String json = JsonParserUtil.parseJsonToObject(UPDATE_USER_JSON).toString();
        mockMvc.perform(put(API_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserDeleteUserThenReturnStatusIsOk() throws Exception {
        User user = userRepository.save(new User("user2", "user2@gmail.com", passwordEncoder.encode("0plhrtff"),
                Collections.emptySet()));
        mockMvc.perform(delete(API_USER + "/{id}", user.getUserId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserGetHisWalletsThenReturnStatusIsOk() throws Exception {
        User user = userRepository.save(new User("user2", "user2@gmail.com", passwordEncoder.encode("0plhrtff"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.EUR, 1000D));
                }}));
        mockMvc.perform(get(API_USER + "/{id}/wallets", user.getUserId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserTryToAddNewWalletThenReturnStatusIsOk() throws Exception {
        String jsonSrc = JsonParserUtil.parseJsonToObject(NEW_WALLET_JSON).toString();
        User user = userRepository.save(new User("user2", "user2@gmail.com", passwordEncoder.encode("0plhrtff"),
                new HashSet<Wallet>() {{
                    add(new Wallet(Currency.EUR, 1000D));
                }}));
        mockMvc.perform(put(API_USER + "/{id}/wallet/new", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSrc))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenGetAllUsersWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(API_USER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenGetCertainUserWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(API_USER + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUpdateUserWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(put(API_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenDeleteUserWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(delete(API_USER + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenGetUserWalletsWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(API_USER + "/1/wallets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUserAddNewWalletWithAnonymousUserThenReturnStatusIsUnauthorized() throws Exception {
        mockMvc.perform(post(API_USER + "/1/wallet/new")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}
