package com.example.KindergartenBillApp.administration.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KindergartenDto {

    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String logo;

    private Set<ActivityDto> activities;

}
