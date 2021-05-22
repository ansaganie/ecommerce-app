package com.ansagan.ecommerceapp.model.persistence.repositories;

import com.ansagan.ecommerceapp.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
