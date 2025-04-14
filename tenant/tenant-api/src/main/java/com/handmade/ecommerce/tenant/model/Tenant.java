package com.handmade.ecommerce.tenant.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
@Entity
@Table(name = "hm_tenant")
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
public class Tenant extends AbstractJpaStateEntity
{
    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "base_currency")
    private String baseCurrency;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }


    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
