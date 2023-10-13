package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.Repository.WishlistRepository;
import com.peko.houshoukaizokudan.model.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    @Autowired
    private WishlistRepository wishListRepository;

    //addProductToWishList
    public void addProductToWishList(Wishlist wishlist) {
        wishListRepository.save(wishlist);
    }



    //removeProductFromWishList
    public void removeProductFromWishList(Integer productId) {
        wishListRepository.deleteById(productId);
    }


    //getWishList
    public Wishlist getWishList() {
        return wishListRepository.findAll().get(0); //因為唯一，所以只要取第一個即可
    }


}
