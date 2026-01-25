package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "user")
public class User extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "username can not be empty or null")
    @JsonProperty("username")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "password can not be empty or null")
    @JsonProperty("password")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "email can not be empty or null")
    @Email(message = "email format is not valid")
    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "roll id can not be null")
    @JsonProperty("role_id")
    @Column(name = "role_id")
    private Integer roleId;

    @JsonProperty("status")
    @Column(name = "status")
    private Boolean status = true;



}
