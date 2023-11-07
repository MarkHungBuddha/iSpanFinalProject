# iSpanFinalProject
iSpan EEIT71 Team1-3c shopping website

@PostMapping("/public/api/sendEmail")
  寄送註冊認證碼
  
@PostMapping("/public/api/verifyCode")
  確認認證是否成功
  
@GetMapping("/public/api/google-login")
  Google登入
  
@GetMapping("/public/api/google-callback")
  GooGLE重新導向網址
  
@PostMapping("/public/api/member/post")
  會員註冊
  
@PostMapping("/public/api/member/memberLogin")
  會員登入

@PostMapping("/customer/member/logout")
  會員登出

@PostMapping("/public/api/upload")
  會員頭像上傳

@PostMapping("/customer/api/order/checkout")
  會員確認訂單

@PostMapping("/public/api/request")
  修改寄認證信

@PostMapping("/public/api/reset")
  密碼更改成功

@GetMapping("/customer/api/findorders")
  買家找訂單 (page) 不含商品內容

@GetMapping("/customer/api/findAllOrders")
  買家找訂單 (page) 有含商品內容

@GetMapping("/customer/api/findorders/Status")
  買家找訂單 by 訂單狀態 (page)

@PutMapping("/customer/api/order/{id}")
  買家修改訂單地址

@PutMapping("/customer/api/{id}/cancelOrder")
  買家取消訂單 待付款or待出貨 > 取消

@PutMapping("/customer/api/{id}/payOrder")
  買家訂單按付款按鈕 待付款(1) > 待出貨(2)

@PutMapping("/customer/api/{id}/completeOrder")
  買家完成訂單 待收貨(3) > 已完成(4)

@GetMapping("/customer/api/orders/orderDetail")
  買家查詢訂單內容的商品

@GetMapping("/seller/api/findSellerOrders")
  賣家查訂單

@PutMapping("/seller/api/{id}/shipOrder")
  賣家訂單按出貨按紐 待出貨(2) > 待收貨(3)

@PostMapping("/customer/api/order/addOrder")
  買家新增訂單

@GetMapping("/public/api/products")
  模糊搜尋 + 價格範圍 未給頁碼 預設第一頁

@GetMapping("/public/api/categoryname")
  價格範圍搜尋 + 分類名稱(categoryname)搜尋

@GetMapping("/seller/api/products/search")
  賣家分頁顯示上傳商品(搜尋)

@PostMapping("/seller/api/product/{od}")
  賣家新增商品跟圖片(編碼ORDERID)

@GetMapping("/seller/api/products")
  賣家顯示所有上傳

@PutMapping("/seller/api/product/{id}/{od}/editImg")
  賣家更新商品圖片

@PutMapping("/seller/api/product/{od}")
  賣家更新商品資料

@PutMapping("/seller/api/{id}/remove")
  賣家下架商品

@DeleteMapping("/seller/api/{id}/{od}/deleteEachImg")
  賣家刪除商品圖片

@GetMapping("/seller/api/product")
  賣家顯示一個商品資料

@PostMapping("/seller/api/csv")
  賣家csv上傳商品(尚未測試)

@GetMapping("/public/product/{productId}")
  公開顯示商品資料

@GetMapping("/public/productImage/{productid}")
  公開顯示商品圖片

@PostMapping("/customer/api//reviews")
  買家新增商品評價

@PutMapping("/customer/api/reviews/{id}")
  買家編輯商品評價(只能編輯一個月內的訂單的評論)

@GetMapping("/seller/api/reviews/recent/{page}")
  賣家查看最近評論by page

@GetMapping("/public/api/reviews/product/{productId}/average")
  公開查看商品平均評價

@GetMapping("/seller/api/reviews/seller/{sellerId}/monthly")
  賣家查看月度評論分析

@GetMapping("/seller/api/productreview/{productid}")
  賣家查看最近訂單評論

@PostMapping("/customer/api/product/qanda/add/{productid}")
  買家新增問題

@GetMapping("/seller/api/qanda/unanswered")
  賣家查看尚未回答問題

@DeleteMapping("/customer/api/qanda/delete/{qandaId}")
  買家刪除問題

@PutMapping("/customer/api/product/qanda/edit-question/{qandaId}")
  買家編輯問題

@PutMapping("/seller/api/product/qanda/answer/{qandaId}")
  賣家回答問題

@GetMapping("/public/api/product/{productid}/qanda")
  顯示商品所有問答

@GetMapping("/seller/api/revenue/year")
  賣家顯示年營收

@GetMapping("/seller/api/revenue/month")
  賣家顯示月營收

@GetMapping("/customer/api/shoppingCart")
  買家查看購物車

@PostMapping("/customer/api/shoppingCart")
  買家新增商品到購物車

@DeleteMapping("/customer/api/shoppingCart")
  買家移除商品

@PutMapping("/customer/api/change")
  買家更改商品數量

@PostMapping("/customer/api/wishlist/{productId}")
  買家新增商品進願望清單

@DeleteMapping("/customer/api/wishlist/{productId}")
  買家從願望清單刪除商品

@GetMapping("/customer/api/wishlist")
  買家查看願望清單商品

@GetMapping("/customer/api/member/types/{id}")
查詢會員型別

@PutMapping("/customer/api/member/{userId}/type/{typeId}")  
修改會員型別

@GetMapping("/customer/api/member/{id}")
查詢會員資料

@PutMapping("/customer/api/member/update/{id}")
修改會員資料

@GetMapping("/public/api/checkLoginStatus")
查詢登入狀態

@GetMapping("/customer/api/findOneOrder")
買家找一筆訂單 (page) 有含商品內容

@PostMapping("/public/api/member/upload")
上傳會員大頭照

@PostMapping("/public/api/member/post")
更新只需輸入帳號密碼信箱

 @DeleteMapping("/member/api/member/{id}")
 刪除會員
 
@GetMapping("/public/api/currentUser")
已登入會員可獲取ID(無實裝在前端)

 @PostMapping("/public/api/request")
 忘記密碼信件寄出
 key:email value:XXX@XXX.com

@PostMapping("/public/api/reset")
修改密碼 key: email resetToken newPassword

@GetMapping("/customer/api/findOneOrder")
買家找一筆訂單 (page) 有含商品內容

@GetMapping("/seller/api/sellerfindorders/Status")
賣家找訂單 by 訂單狀態 (page)

@GetMapping("/seller/api/sellerfindOneOrder")
賣家找一筆訂單 (page) 有含商品內容

@PostMapping("/customer/api/sendPhoneVCode")
傳送驗證簡訊   輸入手機號碼(需使用已在資料庫內的號碼) key:mobile value:手機號碼

@PostMapping("/customer/api/PhoneVCode")
驗證手機號碼 驗證成功membertype會改為2 key:mobile             value:手機號碼
                                        verificationCode    value:驗證碼
