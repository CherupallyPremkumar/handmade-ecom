package com.handmade.ecommerce.shipping.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
@Entity
@Table(name = "shipping")
public class Shipping extends AbstractJpaStateEntity
{
	public String assignee;
	public String assignComment;
	public String closeComment;
	public String resolveComment;
	public String description;
	public String openedBy;
}
