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
    private Integer rating;
    private String reviewcontent;
    private String reviewtime;

}
