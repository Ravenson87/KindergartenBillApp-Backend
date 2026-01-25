package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "bill")
public class Bill extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "year can not be null")
    @JsonProperty("year")
    @Column(name = "year", nullable = false)
    private Short year;

    @NotBlank(message = "month can not be empty or null")
    @JsonProperty("month")
    @Column(name = "month", nullable = false)
    private String month;

    @JsonProperty("deadline")
    @Column(name = "deadline")
    private LocalDate deadline;

    @JsonProperty("bill_code")
    @Column(name = "bill_code")
    private String billCode;

    @JsonProperty("payment_sum")
    @Column(name = "payment_sum")
    private BigDecimal paymentSum = BigDecimal.ZERO;

    @NotNull(message = "kindergarten can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kindergarten_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Kindergarten kindergarten;

    @NotNull(message = "child can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Child child;

}
