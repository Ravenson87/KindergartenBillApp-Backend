package com.example.KindergartenBillApp.administration.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupsIdsDto {

    @NotNull(message = "Group id cannot be null")
    @Min(value = 1, message = "Group id must be greater than zero")
    private Integer groupId;

    public GroupsIdsDto(Integer groupId) {
        this.groupId = groupId;
    }


}
