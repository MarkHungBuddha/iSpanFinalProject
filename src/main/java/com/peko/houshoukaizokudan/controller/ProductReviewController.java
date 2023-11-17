package com.peko.houshoukaizokudan.controller;


import com.peko.houshoukaizokudan.DTO.ProductReviewDTO;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductReview;
import com.peko.houshoukaizokudan.service.ProductReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;


    //      POST /api/v1/reviews：買家新增商品評價
//              1.api 傳入 orderid orderdetailid productid memberid
//              2.檢查這筆訂單是不是這個會員買的(orderid,memberid)
//              3.檢查這筆訂單有沒有買這個商品(orderdetailid,productid)
//              4.建立新的productreview
    @PostMapping("/customer/api/reviews")
    public ResponseEntity<?> createProductReview(@RequestBody ProductReviewDTO productReviewDTO, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser"); // 从session中获取登录用户

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录");
        }

        // 检查这笔订单是不是这个会员买的
        if (!productReviewService.isOrderBelongToMember(productReviewDTO.getOrderid(), loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("这笔订单不属于当前用户");
        }

        // 检查这笔订单有没有买这个商品
        if (!productReviewService.isProductInOrder(
                productReviewService.getOrderDetailIdByOrderIdAndProductId(productReviewDTO.getOrderid(), productReviewDTO.getProductid()),
                productReviewDTO.getProductid()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("这笔订单中没有这个商品");
        }

        if(!productReviewService.isProductInOrder(
                productReviewService.getOrderDetailIdByOrderIdAndProductId(productReviewDTO.getOrderid(),productReviewDTO.getProductid()),
                productReviewDTO.getProductid()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("这笔订单中未完成");

        if(productReviewService.hasBuyerReviewed(loginUser.getId(),productReviewDTO.getOrderdetailid()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("已經評論過這項商品");
        // 建立新的productreview
        productReviewService.createReview(productReviewDTO, loginUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/api/review/checkreviewed")
    public boolean BuyerReviewedCheck(@RequestParam("orderDetailid")Integer orderDetailid,HttpSession session){
        Member loginUser = (Member) session.getAttribute("loginUser"); // 从session中获取登录用户
        System.out.println("loginUserID="+loginUser.getId());
        System.out.println("orederDetailid:"+orderDetailid);
        return productReviewService.hasBuyerReviewed(loginUser.getId(),orderDetailid);
    }

    @GetMapping("/customer/api/order/{orderid}/status")
    public boolean getOrderStatus(@PathVariable Integer orderid, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser"); // 从session中获取登录用户
        if (loginUser == null) {
            return false;
        }
        return productReviewService.isOrderStatusFinish(orderid);
    }

    @GetMapping("/customer/api/status/{orderid}")
    public boolean getReviewStatus(@PathVariable Integer orderid, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser"); // 从session中获取登录用户
        if (loginUser == null) {
            return false;
        }
        if (!productReviewService.isOrderStatusFinish(orderid)) {
            return false;
        }
        return true;
    }




    //    PUT /api/v1/reviews/:id：買家編輯商品評價(只能編輯一個月內的訂單的評論)
    @PutMapping("/customer/api/reviews/{id}")
    public ResponseEntity<?> updateProductReview(@PathVariable Integer id, @RequestBody ProductReviewDTO productReviewDTO, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser"); // 从session中获取登录用户

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录");
        }

        // 检查要编辑的评价是否属于当前用户
        if (!productReviewService.isReviewBelongToMember(id, loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("这条评价不属于当前用户");
        }

        // 更新评价
        productReviewService.updateReview(id, productReviewDTO);

        return ResponseEntity.ok().build();
    }

    //賣家查看最近評論by page
    @GetMapping("/seller/api/reviews/recent/{page}")
    public ResponseEntity<?> getRecentReviews(HttpSession session, @PathVariable Integer page) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        Integer sellerId = loginUser.getId(); // 从session中获取登录用户的sellerId
        List<ProductReview> recentReviews = productReviewService.getRecentReviewsForSeller(sellerId, page);
        return ResponseEntity.ok().body(recentReviews);
    }


    //    GET /public/api/reviews/product/{productId}/averag：查看商品平均評價
    @GetMapping("/public/api/reviews/product/{productId}/average")
    public ResponseEntity<?> getProductAverageReview(@PathVariable Integer productId) {
        Map<String, Object> averageData = productReviewService.getProductAverageReview(productId);
        return ResponseEntity.ok().body(averageData);
    }

    //    GET /public/api/reviews/product/{productId}：查看全部評價
    @GetMapping("/public/api/reviews/product/{productId}")
    public ResponseEntity<?> getProductReviews(@PathVariable Integer productId) {
        List<ProductReviewDTO> productReviews = productReviewService.findProductReviewByProductid(productId);
        return ResponseEntity.ok().body(productReviews);
    }

    //    GET /seller/api/reviews：查看全部評價
    @GetMapping("/seller/api/reviews")
    public ResponseEntity<?> getSellerReviews(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        List <ProductReviewDTO> sellerReviews = productReviewService.getSellerReviews(loginUser.getId());
        return ResponseEntity.ok().body(sellerReviews);
    }

    //    GET /customer/api/reviews：查看全部評價
    @GetMapping("/customer/api/reviews")
    public ResponseEntity<?> getCustomerReviews(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        List <ProductReviewDTO> customerReviews = productReviewService.getCustomerReviews(loginUser.getId());
        return ResponseEntity.ok().body(customerReviews);
    }


    //    GET /api/v1/reviews/seller/:sellerId/monthly：賣家查看月度評論分析
    @GetMapping("/seller/api/reviews/seller/{sellerId}/monthly")
    public ResponseEntity<?> getSellerMonthlyReview(@PathVariable Integer sellerId, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.getId().equals(sellerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录或没有权限");
        }
        Map<String, Object> monthlyData = productReviewService.getSellerMonthlyReview(sellerId);
        return ResponseEntity.ok().body(monthlyData);
    }


    //product review by time 賣家查看最近訂單評論
    @GetMapping("/seller/api/productreview/{productid}")
    public ResponseEntity<List<ProductReviewDTO>> findProductReviewByProductid(@PathVariable Integer productid) {
        try {
            List<ProductReviewDTO> productReviews = productReviewService.findProductReviewByProductid(productid);
            return ResponseEntity.ok(productReviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }




}
