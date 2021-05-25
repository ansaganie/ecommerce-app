package com.ansagan.ecommerceapp.repositories;


import com.ansagan.ecommerceapp.model.Cart;
import com.ansagan.ecommerceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
