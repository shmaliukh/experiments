package com.vshmaliukh.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product_types")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id", nullable = false)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "Product_type_Items",
            joinColumns = @JoinColumn(
                    name = "product_type_id",
                    referencedColumnName = "product_type_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "item_id")
    )
    private List<Item> itemList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "Product_type_Fields",
            joinColumns = @JoinColumn(
                    name = "product_type_id",
                    referencedColumnName = "product_type_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "custom_field_id",
                    referencedColumnName = "custom_field_id")
    )
    private List<CustomItemField> fieldList;

}
