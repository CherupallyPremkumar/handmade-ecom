package com.handmade.ecommerce.order.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
@Entity
@Table(name = "orders")
public class Order extends AbstractJpaStateEntity
{

}
