package com.example.KindergartenBillApp.administration.model;

import com.example.KindergartenBillApp.sharedTools.models.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "kindergarten" )
public class Kindergarten extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "name can not be empty or null")
    @JsonProperty("name")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Preporuceno mi je da ovo direktno zamenim JOIN-ovanom kolonom iz razloga sto
     * je tako pregledije i ORM alat tada sam radi sta je potrebno, a nema potrebe
     * da imam i account_id i join-ovan objekat iz nekoliko razloga:
     * Dva koja se meni cine najbitnija jesu da ne bi doslo do preklapanja
     * i da ne bih bespotrebno svaki put pozivao i join-ovan objekat kada zovem ovu klasu
     * (to se resi oznakom Lazy na fetch-u, sto znaci da se objekat pridruzuje
     * samo kada se direktno pociva (getAccount())
     */
//    @NotNull(message = "account id can not be null")
//    @JsonProperty("account_id")
//    @Column(name = "account_id")
//    private Integer accountId;

    @NotBlank(message = "address can not be empty or null")
    @JsonProperty("address")
    @Column(name = "address",  nullable = false)
    private String address;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank(message = "email can not be empty or null")
    @Email(message = "email format is not valid")
    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty("logo")
    @Column(name = "logo")
    private String logo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "kindergarten_groups",
            joinColumns = @JoinColumn(name = "kindergarten_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Group> group = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "kindergarten_activity",
            joinColumns = @JoinColumn(name = "kindergarten_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private Set<Activity> activities = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Kindergarten)) return false;
        Kindergarten other = (Kindergarten) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }

}
