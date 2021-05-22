package com.ansagan.ecommerceapp.controller;

import java.util.List;

import com.ansagan.ecommerceapp.model.persistence.User;
import com.ansagan.ecommerceapp.model.persistence.UserOrder;
import com.ansagan.ecommerceapp.model.persistence.repositories.OrderRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

	private final Logger log = LoggerFactory.getLogger(OrderController.class);

	public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {



		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[SUBMIT ORDER] [Fail] for user : " + username +", REASON : User not found" );
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.info("[SUBMIT ORDER] [Success] for user : " + user.getUsername());

		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[ORDER HISTORY] [Fail] for user : " + username +", REASON : User not found" );
			return ResponseEntity.notFound().build();
		}

		List<UserOrder> history = orderRepository.findByUser(user);

		log.info("[ORDER HISTORY] [Success] for user : " + user.getUsername());
		return ResponseEntity.ok(history);
	}
}
