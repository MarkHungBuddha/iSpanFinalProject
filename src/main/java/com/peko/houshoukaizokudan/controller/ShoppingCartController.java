package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/shoppingcart")
@SessionAttributes(value="shoppingcart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    
//    @RequestMapping("/shoppingCart")
//    public String shoppingCart(Integer id,HttpSession session,String color,String size){
//        Integer ids=Integer.valueOf(id);
      //根据id获取商品对象
//        List<Map<String, Object>>list = new ArrayList<>();
//        System.out.println("商品列表:"+list);
//        ProductBasic product = new ProductBasic();
//        product.setPicpath((String) list.get(0).get("picpath"));
//        product.setColor(color);
//        product.setSize(size);
//        product.setName((String)list.get(0).get("name"));
//        product.setPrice((Double) list.get(0).get("price"));
//        product.setId((Integer) list.get(0).get("id"));
//        return "shoppingcart/shoppingCart";
//    }
 // 显示购物车
    @GetMapping("/shoppingcart/view")
    public String viewCart() {
        // 从Session中获取购物车对象
    	
        return "shoppingcart/shoppingCart"; // 对应购物车视图的HTML模板
    }

    @RequestMapping(value = "/shoppingcart/addProduct", method = RequestMethod.POST)
    public @ResponseBody String addProductToCart(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("price") BigDecimal price,HttpSession httpsession) {
        // 檢查Session中是否已經存在購物車
//        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ShoppingCart();
//            session.setAttribute("cart", cart);
//        }
    	
//    	session.setAttribute("id", id);
//    	HttpSession session = session.getSession("id");
    	
//    	ProductBasic product = (ProductBasic) session.getAttribute("id");
//    	System.out.print(product);
    	ProductBasic product = new ProductBasic();
    	product.setId(id);
    	product.setProductname(name);
    	product.setPrice(price);
    	httpsession.setAttribute("product", product);
    	System.out.print(httpsession.getAttribute("product"));
    	if (httpsession.getAttribute("product") == null) {
    		return "123";
    	}
    	else {
    	return "456˙ˊ";
    	}

    }
    
    @RequestMapping(value = "/shoppingcart/viewProduct", method = RequestMethod.GET)
    public @ResponseBody String viewProduct(HttpSession httpsession) {

        return "viewproduct";
        
    }

}