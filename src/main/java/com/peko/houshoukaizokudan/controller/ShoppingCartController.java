package com.peko.houshoukaizokudan.controller;
import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    private ProductBasicService productBasicService;



    @PostMapping("/addProduct")
    public void addProductToCart(@RequestBody ProductBasic product, HttpSession session) {
        // 檢查Session中是否已經存在購物車
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        // 將商品添加到購物車
        shoppingCartService.addProductToCart(product);
    }
    // 更新購物車
    @PutMapping("/update")
    public void updateCart(@RequestBody ShoppingCart cartItem) {
        shoppingCartService.updateCart(cartItem);
    }

    // 移除商品
    @DeleteMapping("/removeProduct/{productId}")
    public void removeProductFromCart(@PathVariable Integer productId) {
        shoppingCartService.removeProductFromCart(productId);
    }

    // 清空購物車
    @DeleteMapping("/clear")
    public void clearCart() {
        shoppingCartService.clearCart();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductBasic>> getAllProducts() {
        List<ProductBasic> products = productBasicService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/cart")
    public List<ShoppingCartDto> getShoppingCart(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            Integer memberId = loginUser.getId();
            List<ShoppingCartDto> cartItems = shoppingCartService.GetCartItem(memberId);
            return cartItems;
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") Integer productId, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        try {
            if (loginUser != null) {
                ProductBasic product = productBasicService.findById(productId);
                if (product.getQuantity()<1) {
                    return ResponseEntity.badRequest().body("庫存不足！");
                } else {
                    int a = product.getId();
                    int b = loginUser.getId();
                    shoppingCartService.addProductToCart(b,a);
                    return ResponseEntity.ok(loginUser.getId() + " 已新增 " + productId + " 商品");
                }
            } else {
                return ResponseEntity.badRequest().body("尚未登入");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);

        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("transactionId") Integer transactionId,
                                                        HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            shoppingCartService.ClearCartItem(loginUser, transactionId);
            return ResponseEntity.ok("商品已從購物車中移除");
        } else {
            return ResponseEntity.badRequest().body("尚未登入");
        }
    }

    @PutMapping("/change")
    public ResponseEntity<ShoppingCartDto> changeQuantity(
            @RequestParam("quantity") Integer quantity,
            @RequestParam("productid") Integer productid,
            HttpSession session)
            throws Exception {
        Member loginUser = (Member) session.getAttribute("loginUser");

        System.out.println("transactionId:"+productid);
        System.out.println("quantity"+quantity);
        if (loginUser != null) {
            if (quantity == 0) {
                shoppingCartService.ClearCartItembyProductId(loginUser, productid);
            }
            int memberid = loginUser.getId();
            System.out.println("loginUser.getId"+memberid);

            ShoppingCartDto updatedCart = shoppingCartService.changeQuantity(memberid, productid,
                    quantity);
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.badRequest().build();
    }

}

