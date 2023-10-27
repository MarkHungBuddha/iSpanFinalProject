package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        shoppingCartRepository.save(cartItem);
    	}
    }

    public List<ShoppingCartDto> GetCartItem(Integer memberid) {
        List<Object[]> results = shoppingCartRepository.GetCartItem(memberid);

        List<ShoppingCartDto> cartItems = new ArrayList<>();
        for (Object[] result : results) {
        	ShoppingCartDto cartItem = new ShoppingCartDto();
            cartItem.setTransactionId((Integer) result[0]);
            cartItem.setProductId((Integer) result[1]);
            cartItem.setMemberid((Integer) result[2]);
            cartItem.setProductname((String) result[3]);
            cartItem.setQuantity((Integer) result[4]);
            cartItem.setPrice((BigDecimal) result[5]);

            cartItems.add(cartItem);
        }

        return cartItems;
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