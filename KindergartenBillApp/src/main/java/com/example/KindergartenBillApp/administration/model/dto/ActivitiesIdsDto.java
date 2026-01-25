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
public class ActivitiesIdsDto {

    @NotNull(message = "Activity id cannot be null")
    @Min(value = 1, message = "Activity id must be greater than zero")
    private Integer activitiesId;

    public ActivitiesIdsDto(Integer activitiesId) {
        this.activitiesId = activitiesId;
    }

}
