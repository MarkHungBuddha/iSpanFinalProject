package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.handler.NotEnoughProductsInStockException;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ShoppingCartController {

	@Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductBasicService productBasicService;
    
	@Autowired
	private MemberRepository usersRepo;


    @GetMapping("/shoppingcart/view")
    public String shoppingCart(Model model) {
        List<ProductBasic> products = productBasicService.listAllProducts();
        model.addAttribute("products", products);
        return "shoppingcart/shoppingCart";
    }
    
    @GetMapping("/shoppingcart/viewCart")
    public String viewCart(Model model) {
    	return "shoppingcart/viewCart";
    }

    @GetMapping("/shoppingcart/addproduct")
    public String addProductToCart(@RequestParam("productId") Integer productId,HttpSession session, Model model) {
    	Member dbUser = usersRepo.findByUsername("Elian11");
    	
    	session.setAttribute("loginUser", dbUser);
    	    Member loginUser = (Member) session.getAttribute("loginUser");
    	    if (loginUser != null) {
    	    	ProductBasic product = productBasicService.findById(productId);
    	    	shoppingCartService.addProductToCart(loginUser,product);
    	    	model.addAttribute("title",loginUser.getId() + "已新增" + productId + "商品");
    	    	List<ProductBasic> products = productBasicService.listAllProducts();
    	        model.addAttribute("products", products);
    	        return "shoppingcart/shoppingCart";
    	    } else {
    	    	model.addAttribute("title","尚未登入");
    	        return "shoppingcart/shoppingCart";
    	    }
    }
//
//    @GetMapping("/shoppingCart/removeProduct/{productId}")
//    public ModelAndView removeProductFromCart(@PathVariable("productId") Integer productId) {
//        productBasicService.findById(productId).ifPresent(shoppingCartService::removeProduct);
//        return shoppingCart();
//    }

//    @GetMapping("/shoppingCart/checkout")
//    public ModelAndView checkout() {
//        try {
//            shoppingCartService.checkout();
//        } catch (NotEnoughProductsInStockException e) {
//            return shoppingCart().addObject("outOfStockMessage", e.getMessage());
//        }
//        return shoppingCart();
//    }
}