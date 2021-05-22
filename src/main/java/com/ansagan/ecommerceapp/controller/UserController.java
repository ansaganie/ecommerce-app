package com.ansagan.ecommerceapp.controller;

import com.ansagan.ecommerceapp.model.persistence.Cart;
import com.ansagan.ecommerceapp.model.persistence.User;
import com.ansagan.ecommerceapp.model.persistence.repositories.CartRepository;
import com.ansagan.ecommerceapp.model.persistence.repositories.UserRepository;
import com.ansagan.ecommerceapp.model.requests.CreateUserRequest;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserRepository userRepository;
	private CartRepository cartRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController(UserRepository userRepository, CartRepository cartRepository,
						  BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

    public UserController() {
    }

    @GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		Cart cart = new Cart();

		cartRepository.save(cart);
		user.setCart(cart);

		if(createUserRequest.getPassword().length() < 7 ){
			log.error("[CREATE USER] [Fail] for user : " + user.getUsername() +", REASON : invalid password" );
			return ResponseEntity.badRequest().body("Password must be at least 7 characters.");
		}else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("[CREATE USER] [Fail] for user : " + user.getUsername() +", REASON : password mismatching" );
			return ResponseEntity.badRequest().body("Password field does not match confirm password field");
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		userRepository.save(user);

		log.info("[CREATE USER] [Success] for user : " + user.getUsername());

		return ResponseEntity.ok(user);
	}
	
}
