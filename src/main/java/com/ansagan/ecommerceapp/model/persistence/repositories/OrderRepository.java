package com.ansagan.ecommerceapp.model.persistence.repositories;

import java.util.List;

import com.ansagan.ecommerceapp.model.persistence.User;
import com.ansagan.ecommerceapp.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
