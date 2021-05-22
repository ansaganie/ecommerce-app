package com.ansagan.ecommerceapp.model.persistence.repositories;

import java.util.List;

import com.ansagan.ecommerceapp.model.persistence.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByName(String name);

}
