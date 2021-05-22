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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(CartController.class);

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
			log.error("[ADD TO CART] [Fail] for user : " + request.getUsername() +", REASON : User not found" );
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("[ADD TO CART] [Fail] for item : " + request.getItemId() +", REASON : Item not found" );
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		if (isRemoving) {
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.removeItem(item.get()));
			log.info("[REMOVE FROM CART] [Success] for user : " + user.getUsername());
		} else {
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.addItem(item.get()));
			log.info("[ADD TO CART] [Success] for user : " + user.getUsername());
		}
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
}
