package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import com.peko.houshoukaizokudan.handler.NotEnoughProductsInStockException;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import com.peko.houshoukaizokudan.service.ProductBasicService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String viewCart(Model model,HttpSession session) {
    	Member dbUser = usersRepo.findByUsername("Elian11");
    	session.setAttribute("loginUser", dbUser);
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		List<ShoppingCart> CartItems = shoppingCartService.GetCartItem(loginUser);
    		model.addAttribute("CartItems", CartItems);
    		return "shoppingcart/viewCart";
    	}
    	else {
	        return "shoppingcart/shoppingCart";
	    }
    	
    }
    
    
    @GetMapping("/shoppingcart/MinusCartItem")
    public String MinusCartItem(@RequestParam("transactionId") Integer transactionId,Model model,HttpSession session) {
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		Integer quantity = shoppingCartService.CheckCartItem(transactionId); 
    		if (quantity == 1) {
    			shoppingCartService.ClearCartItem(loginUser,transactionId);
    		}
    		else {
    			shoppingCartService.MinusCartItem(loginUser,transactionId);
    		}
    		List<ShoppingCart> CartItems = shoppingCartService.GetCartItem(loginUser);
    		model.addAttribute("CartItems", CartItems);
    		return "shoppingcart/viewCart";
    	}
    	else {
	        return "shoppingcart/shoppingCart";
	    }
    	
    }
    
    @GetMapping("/shoppingcart/PlusCartItem")
    public String PlusCartItem(@RequestParam("transactionId") Integer transactionId,Model model,HttpSession session) {
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		Integer ProductId = shoppingCartService.GetProductId(transactionId);
    		Integer CartQuantity = shoppingCartService.CheckQuantityByMember(loginUser.getId(),ProductId);
    		ProductBasic product = productBasicService.findById(ProductId);
	    	if (CartQuantity == product.getQuantity()) {
	    		model.addAttribute("title","不可購買超出庫存上限");
	    	}
	    	else {
    		shoppingCartService.PlusCartItem(loginUser,transactionId);
	    	}
    		List<ShoppingCart> CartItems = shoppingCartService.GetCartItem(loginUser);
    		model.addAttribute("CartItems", CartItems);
    		model.addAttribute("title","");
    		return "shoppingcart/viewCart";
    	}
    	else {
	        return "shoppingcart/shoppingCart";
	    }
    	
    }
    
    @GetMapping("/shoppingcart/ClearCartItem")
    public String ClearCartItem(@RequestParam("transactionId") Integer transactionId,Model model,HttpSession session) {
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		shoppingCartService.ClearCartItem(loginUser,transactionId);
    		List<ShoppingCart> CartItems = shoppingCartService.GetCartItem(loginUser);
    		model.addAttribute("CartItems", CartItems);
    		return "shoppingcart/viewCart";
    	}
    	else {
	        return "shoppingcart/shoppingCart";
	    }
    	
    }

    @GetMapping("/shoppingcart/addproduct")
    public String addProductToCart(@RequestParam("productId") Integer productId,HttpSession session, Model model) {
    	Member dbUser = usersRepo.findByUsername("Elian11");
    	
    	session.setAttribute("loginUser", dbUser);
    	    Member loginUser = (Member) session.getAttribute("loginUser");
    	    if (loginUser != null) {
    	    	Integer Quantity = shoppingCartService.CheckQuantityByMember(loginUser.getId(),productId);
    	    	ProductBasic product = productBasicService.findById(productId);
    	    	if (Quantity == product.getQuantity()) {
    	    		model.addAttribute("title","不可購買超出庫存上限");
    	    	}
    	    	else {
    	    		shoppingCartService.addProductToCart(loginUser,product);
    	    		model.addAttribute("title",loginUser.getId() + "已新增" + productId + "商品");
    	    	}
    	    	List<ProductBasic> products = productBasicService.listAllProducts();
    	        model.addAttribute("products", products);
    	        return "shoppingcart/shoppingCart";
    	    } else {
    	    	model.addAttribute("title","尚未登入");
    	        return "shoppingcart/shoppingCart";
    	    }
    }
    
}