package com.peko.houshoukaizokudan.DTO;
import com.peko.houshoukaizokudan.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {


        private Integer id;
        private String passwdbcrypt;
        private String memberimgpath;
        private String username;
        private String firstname;
        private String lastname;
        private String gender;
        private String birthdate;
        private String phone;
        private String email;
        private String membercreationdate;
        private String country;
        private String city;
        private String region;
        private String street;
        private String postalcode;
        private Integer membertypeid;
        private String membertypename;
        private String memberTypeDescription;
        private String resetToken;

        // 其他字段

        // 构造函数、getter 和 setter 省略
    }