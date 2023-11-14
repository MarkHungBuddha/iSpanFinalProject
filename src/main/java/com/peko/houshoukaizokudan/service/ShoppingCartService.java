package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import com.peko.houshoukaizokudan.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    
    

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    // 新增商品進購物車
    public void addProductToCart(ProductBasic product) {
        // 在這裡，您可以執行業務邏輯，例如檢查庫存、計算價格等

        // 創建購物車項目
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setProductid(product);
        cartItem.setQuantity(1); // 假設預設數量是1

        // 將購物車項目保存到資料庫
        shoppingCartRepository.save(cartItem);
    }

    // 更新購物車
    public void updateCart(ShoppingCart cartItem) {
        // 在這裡，您可以執行業務邏輯，例如檢查庫存、計算價格等

        // 更新購物車項目
        shoppingCartRepository.save(cartItem);
    }

    // 移除商品
    public void removeProductFromCart(Integer productId) {
        // 根據商品ID查找購物車項目並刪除
        shoppingCartRepository.deleteById(productId);
    }

//    // 清空購物車
//    public void clearCart() {
//        // 刪除購物車中的所有項目
//        shoppingCartRepository.deleteAll();
//    }

    @Transactional
    public void addProductToCart(Integer memberid, Integer productid) {
    	Integer newquantity = shoppingCartRepository.findquantityByMemberid_IdAndProductid_Id(productid,memberid);
    	Integer oldquantity = pbRepo.findProductByProductid(productid);
        if (shoppingCartRepository.existsByMemberid_IdAndProductid_Id(memberid, productid)) {
        	if(newquantity>=oldquantity) {
        		return; 
        	}
            shoppingCartRepository.saveProductFromShoppingCart(
                    productid,
                    memberid,
                    newquantity+1
            );
            return;
        }
            ShoppingCart cartItem = new ShoppingCart();
            Optional<Member> member = mbRepo.findById(memberid);
            Optional<ProductBasic> product = pbRepo.findById(productid);
            cartItem.setMemberid(member.get());
            cartItem.setProductid(product.get());
            cartItem.setQuantity(1);
            shoppingCartRepository.save(cartItem);
    }

    public List<ShoppingCartDto> getCartItemsByMemberId(Integer memberid) {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.getCartItemsByMemberId(memberid);

        List<ShoppingCartDto> cartItems = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            ShoppingCartDto cartItem = new ShoppingCartDto();
            cartItem.setTransactionId(cart.getId());
            cartItem.setProductId(cart.getProductid().getId());
            cartItem.setMemberid(cart.getMemberid().getId());
            cartItem.setProductname(cart.getProductid().getProductname());
            cartItem.setQuantity(cart.getQuantity());
            cartItem.setPrice(cart.getProductid().getPrice());
            cartItem.setSpecialprice(cart.getProductid().getSpecialprice());
            // Note: You have to fetch 'specialprice' similarly from ProductBasic if it exists.
            String imagePath = piRepo.findImagepathByProductid(cart.getProductid().getId());
            cartItem.setImagepath(imagePath);

            cartItems.add(cartItem);
        }
        return cartItems;
    }




    public void ClearCartItem(Member member,Integer transactionid) {

        if(shoppingCartRepository.findmemberidbytransactionid(transactionid) == member.getId()){
        shoppingCartRepository.ClearCartItem(transactionid);

        }
      
    }

    public void ClearCartItembyProductId(Member member,Integer transactionid) {
        shoppingCartRepository.ClearCartItembyProductId(member.getId(),transactionid);
    }

//    public Integer CheckQuantityByMember(Integer memberid,Integer productid) {
//        Integer result = shoppingCartRepository.CheckQuantityByMember(memberid,productid);
//        return result;


    public Integer CheckCartItem(Integer transactionid) {
        Integer result = shoppingCartRepository.CheckCartItemQuantity(transactionid);
        return result;
    }

    public Integer GetProductId(Integer transactionid) {
        Integer result = shoppingCartRepository.GetProductId(transactionid);
        return result;
    }

    //更改購物車數量
    @Transactional
    public ShoppingCartDto changeQuantity(Integer memberid, Integer productid, Integer quantity) throws Exception {
        Integer productQuantity = pbRepo.findQuantityById(productid);  // Assuming this method retrieves the quantity based on productid

        if(quantity > productQuantity) {
            throw new Exception("數量超過庫存上限");
        }

        shoppingCartRepository.saveProductFromShoppingCart(productid, memberid, quantity);

        ShoppingCartDto sc = ShoppingCartDto.builder().memberid(memberid).quantity(quantity).ProductId(productid).build();
//        shoppingCartRepository.updateQuantityByMemberIdAndProductId(memberid, productid, quantity);

        return sc;

    }
}