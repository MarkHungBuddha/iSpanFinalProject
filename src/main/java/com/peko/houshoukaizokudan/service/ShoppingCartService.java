package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private MemberRepository mbRepo;
    @Autowired
    private ProductBasicRepository pbRepo;
    @Autowired
    private ProductImageRepository piRepo;
    
    
    
    @Transactional
    public void addProductToCart(Integer b,Integer a) {
    	
        ShoppingCart cartItem = new ShoppingCart();
        Optional<Member> mb = mbRepo.findById(b);
        Optional<ProductBasic> pb = pbRepo.findById(a);
        
        cartItem.setMemberid(mb.get());
        cartItem.setProductid(pb.get());
        cartItem.setQuantity(1);
        shoppingCartRepository.save(cartItem);
    	
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
            String a = piRepo.findImagepathByProductid((Integer) result[1]);
            cartItem.setImagepath(a);

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
    
//    public Integer CheckQuantityByMember(Integer memberid,Integer productid) {
//        Integer result = shoppingCartRepository.CheckQuantityByMember(memberid,productid);
//        return result;
    
    
    public Integer CheckCartItem(Integer transactionid) {
        Integer result = shoppingCartRepository.CheckCartItem(transactionid);
        return result;
    }
    
    public Integer GetProductId(Integer transactionid) {
    	Integer result = shoppingCartRepository.GetProductId(transactionid);
        return result;
    }
    @Transactional
	public ShoppingCart changeQuantity(Integer c, Integer transactionid, Integer quantity) throws Exception {
    	Integer sb = shoppingCartRepository.GetProductId(transactionid);
    	Integer pb = pbRepo.findQuantityById(sb);
    	
    	if(quantity<pb) {			
    	ShoppingCart sc = shoppingCartRepository.findByIdAndUser(c,transactionid);
 
		sc.setQuantity(quantity);

//        shoppingCartRepository.save(sc); // 保存更新後的實體
        
        shoppingCartRepository.updateQuantityByMemberIdAndProductId(c,sb,quantity);

		return sc;
		
	
		}
    	throw new Exception("數量超過庫存上限");
	}

}