package com.example.KindergartenBillApp.administration.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityDto {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean status;
}
