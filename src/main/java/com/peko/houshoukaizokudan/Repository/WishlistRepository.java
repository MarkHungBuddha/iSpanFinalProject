package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {
}
