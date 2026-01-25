package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "parent")
public class Parent extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "child fullname can not be empty or null")
    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "parent fullname can not be empty or null")
    @JsonProperty("surname")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotBlank(message = "parent email can not be empty or null")
    @Email(message = "email format is not valid")
    @JsonProperty("email")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "address can not be empty or null")
    @JsonProperty("address")
    @Column(name = "address", nullable = false)
    private String address;
}
