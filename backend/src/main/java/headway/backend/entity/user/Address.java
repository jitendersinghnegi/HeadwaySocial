package headway.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    @NotBlank
    @Size(min =5, message = "Street name must be atleast 5 characters")
    @Column(name = "street")
    private String street;
    @Column(name = "building_name")
    private String buildingName;
    @NotBlank
    @Column(name = "city")
    @Size(min =5, message = "City name must be atleast 5 characters")
    private String city;

    @NotBlank
    @Column(name = "state")
    @Size(min =2, message = "State name must be atleast 2 characters")
    private String state;

    @NotBlank
    @Column(name = "country")
    @Size(min =2, message = "Country name must be atleast 5 characters")
    private String country;

    @NotBlank
    @Column(name = "pincode")
    @Size(min =6, message = "pincode must be atleast 6 characters")
    private String pincode;
    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
