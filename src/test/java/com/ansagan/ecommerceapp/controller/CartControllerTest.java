package com.ansagan.ecommerceapp.controller;

import com.ansagan.ecommerceapp.model.persistence.Cart;
import com.ansagan.ecommerceapp.model.persistence.Item;
import com.ansagan.ecommerceapp.model.persistence.repositories.CartRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.ItemRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.UserRepository;
import com.ansagan.ecommerceapp.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.Optional;

import static com.ansagan.ecommerceapp.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){

        when(userRepository.findByUsername("ansagan")).thenReturn(createUser());
        when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));

    }



    @Test
    public void verify_addToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("ansagan");

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = createCart(createUser());

        assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())).add(generatedCart.getTotal());

        assertEquals("ansagan", actualCart.getUser().getUsername());
        assertEquals(generatedCart.getItems().size() + request.getQuantity(), actualCart.getItems().size());
        assertEquals(createItem(1), actualCart.getItems().get(0));
        assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void verify_removeFromCart(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("ansagan");

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = createCart(createUser());

        assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = generatedCart.getTotal().subtract(itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        assertEquals("ansagan", actualCart.getUser().getUsername());
        assertEquals(generatedCart.getItems().size() - request.getQuantity(), actualCart.getItems().size());
        assertEquals(createItem(2), actualCart.getItems().get(0));
        assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void verify_InvalidUsername(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("invalidUser");

        ResponseEntity<Cart> removeResponse = cartController.removeFromCart(request);
        assertNotNull(removeResponse);
        assertEquals(404, removeResponse.getStatusCodeValue());
        assertNull(removeResponse.getBody());

        ResponseEntity<Cart> addResponse = cartController.addToCart(request);
        assertNotNull(addResponse);
        assertEquals(404, addResponse.getStatusCodeValue());
        assertNull(addResponse.getBody());

        verify(userRepository, times(2)).findByUsername("invalidUser");

    }



}
