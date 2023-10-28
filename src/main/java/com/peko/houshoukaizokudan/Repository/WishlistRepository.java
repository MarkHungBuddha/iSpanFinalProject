package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {

    boolean existsByMemberid_IdAndProductid_Id(Integer memberid, Integer productid);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.memberid.id = ?1 AND w.productid.id = ?2")
    void deleteByMemberidAndProductid(Integer memberid, Integer productid);

    @Query("SELECT w.productid.id FROM Wishlist w WHERE w.memberid.id = ?1")
    List<Integer> findProductIdsByMemberid(Integer memberid);

}
