package com.peko.houshoukaizokudan.DTO;


import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductReviewDTO {
    private Integer productid;
    private Integer memberid;
    private Integer orderid; // 新增
    private Integer orderdetailid; // 新增
    private Integer rating;
    private String reviewcontent;
    private String reviewtime;
}

