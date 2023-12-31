package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;

import io.jsonwebtoken.io.IOException;



import java.util.List;

@Service
public class ProductImageService {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private final String CLIENT_ID = "Bearer 95fe35e9dca4195e698ceba640b42a76eb0b56cb";  // 替換為你的 Imgur Client ID


    @Autowired
    private ProductImageRepository productImageRepository;  // 假设你有一个ProductImageRepository用于与数据库交互

    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Transactional
    public List<String> findImageByProductId(Integer productId) {
        return productImageRepository.findImagePathsByProductIdOrderByOrderID(productId);
    }

    public String findFirstImageByProductId(Integer productId){
        return productImageRepository.findByProductIdAndOrderId(productId,1);

    }

    public String uploadImage(MultipartFile file,Integer id,Integer od) throws IOException, java.io.IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 检查文件是否存在
        if (file.isEmpty()) {
            throw new IllegalArgumentException("請上傳一個文件");
        }

        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Client-ID " + CLIENT_ID);

            // 将 MultipartFile 转换为字节数组
            byte[] fileContent = file.getBytes();

            // 创建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

            // 从响应中获取图片网址
            String imageUrl = response.getBody();
            String pattern = "https://i.imgur.com/(\\w{7})\\.(?:jpeg|png|webp)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(imageUrl);

            String extractedCode = "";
            if (m.find()) {
                extractedCode = m.group(1);
            }

            System.out.println(extractedCode);
            saveProductImage(id, extractedCode,od);
            // 保存 ProductImage 到数据库
            System.out.println("Link:"+extractedCode);



            // 返回图片网址
            return imageUrl;
        } catch (IOException e) {
            // 处理错误
            throw new RuntimeException("文件上傳失敗");
        }
    }
    @Transactional
    public String updateImage(MultipartFile file,Integer id,Integer od) throws IOException, java.io.IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 检查文件是否存在
        if (file.isEmpty()) {
            throw new IllegalArgumentException("請上傳一個文件");
        }

        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Client-ID " + CLIENT_ID);

            // 将 MultipartFile 转换为字节数组
            byte[] fileContent = file.getBytes();

            // 创建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

            // 从响应中获取图片网址
            String imageUrl = response.getBody();
            String pattern = "https://i.imgur.com/(\\w{7})\\.(?:jpeg|png)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(imageUrl);

            String extractedCode = "";
            if (m.find()) {
                extractedCode = m.group(1);
            }

            String img= productImageRepository.findByProductIdAndOrderId(id,od);

            if(img==null) {
                saveProductImage(id, extractedCode,od);
                // 保存 ProductImage 到数据库
                System.out.println("Link:"+extractedCode);
            }else {
                productImageRepository.deleteProductImage(id,od);
                saveProductImage(id, extractedCode,od);
            }

            // 返回图片网址
            return imageUrl;
        } catch (IOException e) {
            // 处理错误
            throw new RuntimeException("文件上傳失敗");
        }
    }

    @Transactional
    public void saveProductImage(ProductBasic product,String extractedCode) {
        // 创建 ProductImage 对象并设置相应的字段
        ProductImage productImage = new ProductImage();
        productImage.setProductid(product);
        productImage.setImagepath(extractedCode);

        // 保存 ProductImage
        productImageRepository.save(productImage);
    }


    public void saveProductImage(Integer productId, String imageUrl,Integer od) {
        // 将图片信息保存到数据库，包括productid
        ProductImage productImage = new ProductImage();
        ProductBasic product = productBasicRepository.findById(productId).orElse(null);

        // 设置productImage的属性，包括productid和imagepath
        productImage.setProductid(product);
        productImage.setImagepath(imageUrl);
        productImage.setOrderID(od);
        productImageRepository.save(productImage);
    }
    public ProductImage findImgPathByPid(Integer id) {
        Optional<ProductImage> imageOptional = productImageRepository.findById(id);

        if (imageOptional.isPresent()) {
            return imageOptional.get();
        } else {
            // 如果找不到对应的ProductImage，可以返回null或抛出异常，视情况而定
            return null;
        }
    }
    @Transactional
    public void deleteImgByod(Integer id, Integer od) {
        productImageRepository.deleteById(id,od);

    }
}