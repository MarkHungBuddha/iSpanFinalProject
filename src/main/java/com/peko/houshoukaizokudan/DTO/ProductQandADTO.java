package com.peko.houshoukaizokudan.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductQandADTO {
    private Integer productId;
    private Integer buyerMemberid;
    private String question;
    private String questiontime;
    private Integer sellerMemberid;
    private String answer;
    private String answertime;
}
