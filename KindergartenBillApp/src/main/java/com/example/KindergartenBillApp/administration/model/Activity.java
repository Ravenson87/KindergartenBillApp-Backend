package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "activity")
public class Activity extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "name can not be empty or null")
    @JsonProperty("name")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @DecimalMin(value = "0.0", message = "Price must be positive number")
    @JsonProperty("price")
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    @JsonProperty("status")
    @Column(name = "status")
    private Boolean status = true;

    @ManyToMany(mappedBy = "activities",  fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Child> children = new HashSet<>();

    @ManyToMany(mappedBy = "activities", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Kindergarten> kindergartens = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity other = (Activity) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }

}
