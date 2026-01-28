package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "child")
public class Child extends Auditable implements Serializable {

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

    @Min(value = 1, message = "sibling order must be positive")
    @JsonProperty("sibling_order")
    @Column(name = "sibling_order")
    private Short siblingOrder = 1;

    @JsonProperty("birthday")
    @Column(name = "birthday")
    @PastOrPresent(message = "birthday must be in the past or today")
    private LocalDate birthday;

    @JsonProperty("status")
    @Column(name = "status")
    private Boolean status = true;

    @NotNull(message = "group_id can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Group group;

    @NotNull(message = "parent_id can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Parent parent;

    @NotNull(message = "kindergarten id can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kindergarten_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Kindergarten kindergarten;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "child_activities",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Activity> activities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Child)) return false;
        Child other = (Child) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }


}
