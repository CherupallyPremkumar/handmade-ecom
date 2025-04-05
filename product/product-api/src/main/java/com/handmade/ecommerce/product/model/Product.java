package com.handmade.ecommerce.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.math.BigDecimal;


@Entity
@Table(name = "hm_product")
public class Product extends AbstractJpaStateEntity {

	private String name;
	private String description;
	private BigDecimal price;
	private int stockQuantity;

	private String material;
	private String origin;
	@Column(name = "artisan_id")
	private String artisanId;
	@Column(name = "category_id")
	private String categoryId;
	private String color;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getArtisanId() {
		return artisanId;
	}

	public void setArtisanId(String artisanId) {
		this.artisanId = artisanId;
	}
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	// STM-related fields (inherited from AbstractJpaStateEntity):
	// state, flowId, stateId, stateEntryTime, slaYellowDate, slaRedDate, etc.
}