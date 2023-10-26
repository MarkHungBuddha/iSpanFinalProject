package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private  ShoppingCartRepository shoppingCartRepository;    
    
    public void addProductToCart(Member member,ProductBasic product) {
    	Integer checkcart = shoppingCartRepository.CheckProductByMemberId(member.getId(),product.getId());
    	if (checkcart > 0) {
    		shoppingCartRepository.UpdateCartQuantity(member.getId(),product.getId(),checkcart + 1);
    	}
    	else {
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setMemberid(member);
        cartItem.setProductid(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setProductname(product.getProductname());
        shoppingCartRepository.save(cartItem);
    	}
    }

    public List<ShoppingCart> GetCartItem(Member member) {
        List<ShoppingCart> CartItem = shoppingCartRepository.GetCartItem(member.getId());
        return CartItem;
    }

    public void PlusCartItem(Member member,Integer transactionid) {
        shoppingCartRepository.PlusCartItem(member.getId(),transactionid);
    }
    
    public void MinusCartItem(Member member,Integer transactionid) {
        shoppingCartRepository.MinusCartItem(member.getId(),transactionid);
    }
    
    public void ClearCartItem(Member member,Integer transactionid) {
        shoppingCartRepository.ClearCartItem(member.getId(),transactionid);
    }
    
    public Integer CheckQuantityByMember(Integer memberid,Integer productid) {
        Integer result = shoppingCartRepository.CheckQuantityByMember(memberid,productid);
        return result;
    }
    
    public Integer CheckCartItem(Integer transactionid) {
        Integer result = shoppingCartRepository.CheckCartItem(transactionid);
        return result;
    }
    
    public Integer GetProductId(Integer transactionid) {
    	Integer result = shoppingCartRepository.GetProductId(transactionid);
        return result;
    }

}