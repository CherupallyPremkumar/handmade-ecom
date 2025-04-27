package com.handmade.ecommerce.cart.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hm_cart")
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
public class Cart extends AbstractJpaStateEntity
{
	private String userId;
	private List<CartItem> items;
	private double totalAmount;
	private String status; // ACTIVE, CHECKED_OUT, ABANDONED


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
