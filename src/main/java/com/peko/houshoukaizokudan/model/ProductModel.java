package com.peko.houshoukaizokudan.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.peko.houshoukaizokudan.model.ProductBasic;

public class ProductModel {

	private List<ProductBasic> products;

	public ProductModel() {
		this.products = new ArrayList<ProductBasic>();
//		this.products.add(new ProductBasic(1,"test1",new BigDecimal(45)));
//		this.products.add(new ProductBasic(2,"test2",new BigDecimal(100)));
//		this.products.add(new ProductBasic(3,"test3",new BigDecimal(200)));
	}

	public List<ProductBasic> findAll() {
		return this.products;
	}

	public ProductBasic find(String id) {
		for (ProductBasic product : this.products) {
			if (product.getId().equals(id)) {
				return product;
			}
		}
		return null;
	}

}


