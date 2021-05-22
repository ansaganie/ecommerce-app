package com.ansagan.ecommerceapp.model.persistence.repositories;


import com.ansagan.ecommerceapp.model.persistence.Cart;
import com.ansagan.ecommerceapp.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
