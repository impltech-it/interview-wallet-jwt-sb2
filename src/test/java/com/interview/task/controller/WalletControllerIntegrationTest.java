package com.interview.task.controller;

import com.interview.task.ApplicationRunner;
import com.interview.task.entity.Wallet;
import com.interview.task.enums.Currency;
import com.interview.task.repository.WalletRepository;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ApplicationRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class WalletControllerIntegrationTest {

    private static final String API_WALLET = "/api/wallet";

    private static final String NOT_ALLOWED_JSON = "json/error-operation-is-not-allowed.json";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private WalletRepository walletRepository;

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
        walletRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserAddBalanceThenStatusIsOk() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.EUR, 1350D));
        mockMvc.perform(put(API_WALLET + "/add/{walletId}?amount=441", wallet.getWalletId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserReduceWalletBalanceThenReturnStatusIsOk() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.UAH, 1501D));
        mockMvc.perform(put(API_WALLET + "/reduce/{walletId}?amount=144", wallet.getWalletId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserTryToReplenishOtherUserBalanceThenReturnStatusIsOk() throws Exception {
        Wallet from = walletRepository.save(new Wallet(Currency.UAH, 2000D));
        Wallet to = walletRepository.save(new Wallet(Currency.UAH, 1000D));
        mockMvc.perform(put(API_WALLET + "/{fromId}/replenish/{toId}?amount=700&isMultiCurrency=false",
                from.getWalletId(), to.getWalletId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserDoMultiCurrentReplenishUserBalanceThenReturnStatusIsOk() throws Exception {
        Wallet from = walletRepository.save(new Wallet(Currency.UAH, 28000D));
        Wallet to = walletRepository.save(new Wallet(Currency.USD, 1000D));
        mockMvc.perform(put(API_WALLET + "/{fromId}/replenish/{toId}?amount=200",
                from.getWalletId(), to.getWalletId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserDoMultiCurrentReplenishUserBalanceWithDifferentCurrenciesThenThrowException() throws Exception {
        Wallet from = walletRepository.save(new Wallet(Currency.UAH, 28000D));
        Wallet to = walletRepository.save(new Wallet(Currency.EUR, 1000D));
        String res = mockMvc.perform(put(API_WALLET + "/{fromId}/replenish/{toId}?amount=200",
                from.getWalletId(), to.getWalletId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expected =
                JsonParserUtil.parseJsonToObject(NOT_ALLOWED_JSON).toString();
        JSONAssert.assertEquals(expected, res, JSONCompareMode.LENIENT);
    }

    @Test
    @WithMockUser(value = "testUser", authorities = "USER")
    void whenAuthorizedUserDeleteTheWalletThenReturnStatusIsOk() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.UAH, 28000D));
        mockMvc.perform(delete(API_WALLET + "/{id}", wallet.getWalletId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthorizedUserDeleteTheWalletThenReturnStatusIsUnauthorized() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.UAH, 28000D));
        mockMvc.perform(delete(API_WALLET + "/{id}", wallet.getWalletId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthorizedUserDoMultiCurrentReplenishOtherUserBalanceThenReturnStatusIsUnauthorized() throws Exception {
        Wallet from = walletRepository.save(new Wallet(Currency.UAH, 28000D));
        Wallet to = walletRepository.save(new Wallet(Currency.USD, 1000D));
        mockMvc.perform(put(API_WALLET + "/{fromId}/replenish/{toId}?amount=200",
                from.getWalletId(), to.getWalletId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthorizedUserTryToReplenishOtherUserBalanceThenReturnStatusIsUnauthorized() throws Exception {
        Wallet from = walletRepository.save(new Wallet(Currency.UAH, 2000D));
        Wallet to = walletRepository.save(new Wallet(Currency.UAH, 1000D));
        mockMvc.perform(put(API_WALLET + "/{fromId}/replenish/{toId}?amount=700",
                from.getWalletId(), to.getWalletId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthorizedUserAddBalanceThenStatusIsUnauthorized() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.EUR, 1350D));
        mockMvc.perform(put(API_WALLET + "/addBalance/{walletId}?amount=441", wallet.getWalletId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthorizedUserReduceWalletBalanceThenReturnStatusIsUnauthorized() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet(Currency.UAH, 1501D));
        mockMvc.perform(put(API_WALLET + "/reduce/{walletId}?amount=144", wallet.getWalletId()))
                .andExpect(status().isUnauthorized());
    }
}
