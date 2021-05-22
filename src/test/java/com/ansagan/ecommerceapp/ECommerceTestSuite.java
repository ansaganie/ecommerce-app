package com.ansagan.ecommerceapp;

import com.ansagan.ecommerceapp.controller.CartControllerTest;
import com.ansagan.ecommerceapp.controller.ItemControllerTest;
import com.ansagan.ecommerceapp.controller.OrderControllerTest;
import com.ansagan.ecommerceapp.controller.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {CartControllerTest.class, UserControllerTest.class,
                ItemControllerTest.class, OrderControllerTest.class}
)
public class ECommerceTestSuite {
}
