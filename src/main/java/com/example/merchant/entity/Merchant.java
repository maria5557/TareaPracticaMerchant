package com.example.merchant.entity;

import com.example.merchant.model.MerchantType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Merchant extends MainTable {

    private String name;
    private String address;
    private MerchantType merchantType;
}