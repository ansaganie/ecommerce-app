package com.ansagan.ecommerceapp.controller;

import java.util.Optional;
import java.util.stream.IntStream;

import com.ansagan.ecommerceapp.model.persistence.Cart;
import com.ansagan.ecommerceapp.model.persistence.Item;
import com.ansagan.ecommerceapp.model.persistence.User;
import com.ansagan.ecommerceapp.model.persistence.repositories.CartRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.ItemRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.UserRepository;
import com.ansagan.ecommerceapp.model.requests.ModifyCartRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;

	public CartController(UserRepository userRepository, CartRepository cartRepository, ItemRepository itemRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		return processCartRequest(request, false);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		return processCartRequest(request, true);
	}

	private ResponseEntity<Cart> processCartRequest(ModifyCartRequest request, Boolean isRemoving) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(item.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		if (isRemoving) {
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.removeItem(item.get()));
		} else {
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.addItem(item.get()));
		}
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
}
