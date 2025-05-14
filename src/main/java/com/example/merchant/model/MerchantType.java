package com.example.merchant.model;

import lombok.Getter;

@Getter
public enum MerchantType {
    MERCHANT_TYPE_PERSONAL_SERVICES("Servicios personales"),
    MERCHANT_TYPE_FINANCIAL_SERVICES("Servicios financieros");

    private final String displayName;

    MerchantType(String displayName) {
        this.displayName = displayName;
    }
}
