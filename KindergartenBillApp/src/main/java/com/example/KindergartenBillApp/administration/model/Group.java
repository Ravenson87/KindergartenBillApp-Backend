package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "groups")
public class Group extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "name can not be empty or null")
    @JsonProperty("name")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull(message = "price can not be null")
    @DecimalMin(value = "0.0", message = "price must be positive number")
    @JsonProperty("price")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Min(value = 0, message = "discount must be positive number")
    @JsonProperty("discount")
    @Column(name = "discount")
    private Integer discount = 0;

    @JsonProperty("active")
    @Column(name = "active")
    private Boolean active = true;

    @ManyToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Kindergarten> kindergartens = new HashSet<>();
}

