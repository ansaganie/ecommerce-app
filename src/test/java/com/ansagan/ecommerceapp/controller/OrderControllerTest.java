package com.ansagan.ecommerceapp.controller;

import com.ansagan.ecommerceapp.model.persistence.User;
import com.ansagan.ecommerceapp.model.persistence.UserOrder;
import com.ansagan.ecommerceapp.model.persistence.repositories.OrderRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ansagan.ecommerceapp.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup(){
        User user = createUser();

        when(userRepository.findByUsername("ansagan")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    @Test
    public void verify_submit(){

        ResponseEntity<UserOrder> response = orderController.submit("ansagan");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(createItems(), order.getItems());
        assertEquals(createUser().getId(), order.getUser().getId());


        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void verify_submitInvalid(){

        ResponseEntity<UserOrder> response = orderController.submit("invalid username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull( response.getBody());

        verify(userRepository, times(1)).findByUsername("invalid username");
    }

    @Test
    public void verify_getOrdersForUser(){

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ansagan");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();

        assertNotNull(orders);
        assertEquals(createOrders().size(), orders.size());

    }

    @Test
    public void verify_getOrdersForUserInvalid(){}

}
