package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.WishlistRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishlistRepository wishListRepository;

    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Autowired
    private MemberRepository memberRepository;

    public boolean addProductToWishList(Integer memberid, Integer productid) {
        // Check if the product exists
        if (!productBasicRepository.existsById(productid)) {
            return false;
        }
        // Check if the member exists
        if (!memberRepository.existsById(memberid)) {
            return false;
        }
        // Check if the product is already in the member's wishlist
        if (wishListRepository.existsByMemberid_IdAndProductid_Id(memberid, productid)) {
            return false;
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setMemberid(memberRepository.findById(memberid).orElse(null));
        wishlist.setProductid(productBasicRepository.findById(productid).orElse(null));
        wishListRepository.save(wishlist);
        return true;
    }

    public boolean removeProductFromWishList(Integer memberid, Integer productid) {
        // Check if the product exists
        if (!productBasicRepository.existsById(productid)) {
            return false;
        }
        // Check if the member exists
        if (!memberRepository.existsById(memberid)) {
            return false;
        }
        // Check if the product is in the member's wishlist
        if (!wishListRepository.existsByMemberid_IdAndProductid_Id(memberid, productid)) {
            return false;
        }
        wishListRepository.deleteByMemberidAndProductid(memberid, productid);
        return true;
    }

    public List<Integer> getWishList(Integer memberid) {
        return wishListRepository.findProductIdsByMemberid(memberid);
    }


}
