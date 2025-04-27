package com.handmade.ecommerce.product.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hm_product")
@AttributeOverrides({
	@AttributeOverride(name = "createdTime", column = @Column(name = "created_time")),
	@AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
	@AttributeOverride(name = "lastModifiedTime", column = @Column(name = "last_modified_time")),
	@AttributeOverride(name = "lastModifiedBy", column = @Column(name = "last_modified_by")),
	@AttributeOverride(name = "slaLate", column = @Column(name = "sla_late")),
	@AttributeOverride(name = "slaRedDate", column = @Column(name = "sla_red_date")),
	@AttributeOverride(name = "slaTendingLate", column = @Column(name = "sla_tending_late")),
	@AttributeOverride(name = "slaYellowDate", column = @Column(name = "sla_yellow_date")),
	@AttributeOverride(name = "stateEntryTime", column = @Column(name = "state_entry_time")),
})
public class Product extends AbstractJpaStateEntity {

	private String name;
	private String description;
	private BigDecimal price;
	@Column(name = "stock_quantity")
	private int stockQuantity;
	private String material;
	private String origin;
	@Column(name = "artisan_id")
	private String artisanId;
	@Column(name = "category_id")
	private String categoryId;
	private String color;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ProductPrice> prices = new HashSet<>();

	// Default constructor
	public Product() {
	}

	// Parameterized constructor
	public Product(String name, String description, BigDecimal price, int stockQuantity, 
				  String material, String origin, String artisanId, String categoryId, String color) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.material = material;
		this.origin = origin;
		this.artisanId = artisanId;
		this.categoryId = categoryId;
		this.color = color;
	}

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

	public Set<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(Set<ProductPrice> prices) {
		this.prices = prices;
	}

	public void addPrice(ProductPrice price) {
		prices.add(price);
		price.setProduct(this);
	}

	public void removePrice(ProductPrice price) {
		prices.remove(price);
		price.setProduct(null);
	}

	// STM-related fields (inherited from AbstractJpaStateEntity):
	// state, flowId, stateId, stateEntryTime, slaYellowDate, slaRedDate, etc.
}