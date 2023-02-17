package com.vshmaliukh.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Items")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomItem.class, name = "customItem")
})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    private String name;
    private int price;

}
