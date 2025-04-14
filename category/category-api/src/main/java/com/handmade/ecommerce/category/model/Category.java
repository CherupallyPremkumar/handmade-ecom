package com.handmade.ecommerce.category.model;

import java.io.Serializable;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;


@Entity
@Table(name = "hm_category")
@AttributeOverrides({
        @AttributeOverride(name = "createdTime", column = @Column(name = "created_time")),
        @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
        @AttributeOverride(name = "lastModifiedTime", column = @Column(name = "last_modified_time")),
        @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "last_modified_by")),
})
public class Category extends BaseJpaEntity  {

    @Column(name = "category_name")
    private String categoryName;
    private String description;
     @Column(name = "parent_category_id")
    private String parentCategoryId;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
