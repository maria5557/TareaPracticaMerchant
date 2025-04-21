package com.example.merchant.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
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

    @DynamoDBTypeConvertedEnum
    private MerchantType merchantType;
}