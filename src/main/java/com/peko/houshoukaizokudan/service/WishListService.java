package com.peko.houshoukaizokudan.service;


import com.peko.houshoukaizokudan.DTO.WishlistDTO;
import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.Repository.WishlistRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;
import com.peko.houshoukaizokudan.model.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishlistRepository wishListRepository;

    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    public WishlistDTO addProductToWishList(Integer memberid, Integer productid) throws Exception {
        // 檢查商品是否存在
        if (!productBasicRepository.existsById(productid)) {
            throw new Exception("商品不存在");
        }
        // 檢查會員是否存在
        if (!memberRepository.existsById(memberid)) {
            throw new Exception("會員不存在");
        }
        // Check if the product is already in the member's wishlist
        if (wishListRepository.existsByMemberid_IdAndProductid_Id(memberid, productid)) {
            throw new Exception("商品已存在願望清單");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setMemberid(memberRepository.findById(memberid).orElse(null));
        wishlist.setProductid(productBasicRepository.findById(productid).orElse(null));
        Wishlist savedWishlist = wishListRepository.save(wishlist);
        return convertToDTO(savedWishlist);
    }

    public WishlistDTO removeProductFromWishList(Integer memberid, Integer productid) throws Exception {


        // 檢查商品是否存在
        if (!productBasicRepository.existsById(productid)) {
            throw new Exception("商品不存在");
        }
        // 檢查會員是否存在
        if (!memberRepository.existsById(memberid)) {
            throw new Exception("會員不存在");
        }
        // 檢查商品是否在會員的願望清單中
        if (!wishListRepository.existsByMemberid_IdAndProductid_Id(memberid, productid)) {
            throw new Exception("商品不在會員的願望清單中");
        }



        Wishlist wishlist = wishListRepository.findByMemberid_IdAndProductid_Id(memberid, productid);
        wishListRepository.delete(wishlist);
        return convertToDTO(wishlist);
    }

    public List<WishlistDTO> getWishList(Integer memberid) {
        List<WishlistDTO>wishlistDTOList=new ArrayList<WishlistDTO>();
        List<Wishlist> wishlistList = wishListRepository.findByMemberid_Id(memberid);
        for(Wishlist wishlist:wishlistList){
            WishlistDTO wishlistDTO=new WishlistDTO();
            wishlistDTO.setId(wishlist.getId());
            wishlistDTO.setMemberid(wishlist.getMemberid().getId());
            wishlistDTO.setProductid(wishlist.getProductid().getId());
            wishlistDTO.setProductname(wishlist.getProductid().getProductname());
            wishlistDTO.setProductimage(productImageRepository.findImagepathByProductid(wishlist.getProductid().getId()));
            wishlistDTO.setPrice(wishlist.getProductid().getPrice());
            wishlistDTO.setSpecialprice(wishlist.getProductid().getSpecialprice());
            wishlistDTOList.add(wishlistDTO);
        }
        return wishlistDTOList;
    }

    public WishlistDTO convertToDTO(Wishlist wishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setProductid(wishlist.getProductid().getId());
        wishlistDTO.setProductname(wishlist.getProductid().getProductname());
        wishlistDTO.setProductimage(productImageRepository.findImagepathByProductid(wishlist.getProductid().getId()));
        wishlistDTO.setPrice(wishlist.getProductid().getPrice());
        wishlistDTO.setSpecialprice(wishlist.getProductid().getSpecialprice());
        return wishlistDTO;
    }

    //addProductToWishList
    public void addProductToWishList(Wishlist wishlist) {
        wishListRepository.save(wishlist);
    }



    //removeProductFromWishList
    public void removeProductFromWishList(Integer productId) {
        wishListRepository.deleteById(productId);
    }


    //getWishList
    public Wishlist getWishList() {
        return wishListRepository.findAll().get(0); //因為唯一，所以只要取第一個即可
    }


}
