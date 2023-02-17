package com.vshmaliukh.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Custom_fields")
public class CustomItemField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_field_id", nullable = false)
    private Long id;

//    private

    @ManyToOne
    private CustomItem item;

}
