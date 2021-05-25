package com.ansagan.ecommerceapp.repositories;

import java.util.List;

import com.ansagan.ecommerceapp.model.User;
import com.ansagan.ecommerceapp.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
