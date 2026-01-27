package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "kindergarten_account")
public class KindergartenAccount extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "bank name can not be null or empty")
    @JsonProperty("bank_name")
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @NotBlank(message = "account number can not be null or empty")
    @JsonProperty("account_number")
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @NotBlank(message = "pib can not be null or empty")
    @JsonProperty("pib")
    @Pattern(regexp = "\\d{9}", message = "PIB must have 9 digits")
    @Column(name = "pib", nullable = false)
    private String pib;

    @NotNull(message = "identification number can not be null")
    @JsonProperty("identification_number")
    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;

    @JsonFormat(shape =  JsonFormat.Shape.NUMBER)
    @JsonProperty("activity_code")
    @Column(name = "activity_code")
    private Integer activityCode;

    @NotNull(message = "kindergarten id can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kindergarten_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Kindergarten kindergarten;


}
