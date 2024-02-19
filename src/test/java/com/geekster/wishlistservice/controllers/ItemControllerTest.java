package com.geekster.wishlistservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.geekster.wishlistservice.entity.StoreItem;
import com.geekster.wishlistservice.service.ItemService;
import com.geekster.wishlistservice.utils.dto.GenericSuccessResponse;
import com.geekster.wishlistservice.utils.dto.ItemDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class ItemControllerTest {
    private MockMvc mockMvc;
    private AutoCloseable closeable;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;
// new StoreItem("Black trouser", 39.99, true);
    private final StoreItem ITEM_1 = StoreItem.builder().id(1L).name("Black trouser").price(new BigDecimal(39.99)).inStock(true).build();
    private final StoreItem ITEM_2 = new StoreItem(2L, "Grey T-shirt" , new BigDecimal(29.99), true, new HashSet<>());
    private final StoreItem ITEM_3 = new StoreItem(3L, "Black sweater", new BigDecimal(89.49), false, new HashSet<>());

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @After
    public void cleanUp() throws Exception {
        closeable.close();
    }

    @Test
    public void getAllRecords_success() throws Exception {
        List<StoreItem> records = List.of(ITEM_1, ITEM_2, ITEM_3);
        Mockito.when(itemService.getAllItems())
                .thenReturn(ResponseEntity.ok(records));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is("Black sweater")));

    }

    @Test
    public void getStoreItemById_success() throws Exception {
        Mockito.when(itemService.getItemById(ITEM_1.getId()))
                .thenReturn(ResponseEntity.ok(ITEM_1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue() ))
                .andExpect(jsonPath("$.name", is("Black trouser")));

    }

    @Test
    public void createRecord_success() throws Exception {
        final ItemDto[] dtos = new ItemDto[]{
                new ItemDto(ITEM_1.getName(), ITEM_1.getPrice().doubleValue(), ITEM_1.isInStock()),
                new ItemDto(ITEM_2.getName(), ITEM_2.getPrice().doubleValue(), ITEM_2.isInStock()),
                new ItemDto(ITEM_3.getName(), ITEM_3.getPrice().doubleValue(), ITEM_3.isInStock())
        };
        Mockito.when(itemService.addNewItems(dtos)).thenReturn(ResponseEntity.ok(
                new GenericSuccessResponse<>("Item was added successfully!",
                        Arrays.asList(ITEM_1, ITEM_2, ITEM_3),
                        LocalDateTime.now(),
                        HttpStatus.OK.value()
        )));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(dtos));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue() ))
                .andExpect(jsonPath("$.message", is("Item was added successfully!")));
    }

    @Test
    public void updateRecord_success() throws Exception {
        var dto = new ItemDto(
                ITEM_1.getName(), ITEM_1.getPrice().doubleValue(), true);
        Mockito.when(itemService.updateItemById(1L, dto)).thenReturn(ResponseEntity.ok(
                new GenericSuccessResponse<>("Item was updated successfully",
                        ITEM_1, LocalDateTime.now(), HttpStatus.OK.value()))
        );

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(dto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.target.name", is("Black trouser")));

    }
}


















