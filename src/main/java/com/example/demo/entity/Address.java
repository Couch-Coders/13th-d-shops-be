package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity(name = "ds_address")
@Getter
@Setter
@DynamicInsert //@ColumnDefault("'N'") 할려고
public class Address extends BaseEntiry {
    @Column
    String name;
    @Column
    String post_code;
    @Column
    String address;
    @Column
    String extra;
    @Column
    String detail;
    @Column
    Double location_x; // Longitude
    @Column
    Double location_y; // Latitude
}
