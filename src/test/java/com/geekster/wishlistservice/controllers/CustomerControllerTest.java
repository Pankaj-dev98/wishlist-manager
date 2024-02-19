package com.geekster.wishlistservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.geekster.wishlistservice.entity.Customer;
import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.service.CustomerService;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;
    private AutoCloseable closeable;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    @InjectMocks
    private CustomerController customerController;

    private final StoreItem ITEM_1 = StoreItem.builder().id(1L).name("Black trouser").price(new BigDecimal("39.99")).inStock(true).build();
    private final StoreItem ITEM_2 = new StoreItem(2L, "Grey T-shirt" , new BigDecimal("29.99"), true, new HashSet<>());
    private final StoreItem ITEM_3 = new StoreItem(3L, "Black sweater", new BigDecimal("89.49"), false, new HashSet<>());


    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @After
    public void cleanUp() throws Exception {
        closeable.close();
    }

    @Test
    public void wishlistRead_success() throws Exception {
        Mockito.when(customerService.getWishList())
                .thenReturn(ResponseEntity.ok(List.of(ITEM_1, ITEM_1, ITEM_1)));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/customers/wishlist-items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].name", is("Black trouser")));
    }

    @Test
    public void wishlistAppend_success() throws Exception {
        Mockito.when(customerService.addItemToWishList(1L))
                .thenReturn(ResponseEntity.ok(
                        new GenericSuccessResponse<>(
                                "Item was added successfully to wishlist.",
                                ITEM_1,
                                LocalDateTime.now(),
                                HttpStatus.OK.value()
                        )
                ));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/customers/wishlist-items/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Item was added successfully to wishlist.")));
    }
}

