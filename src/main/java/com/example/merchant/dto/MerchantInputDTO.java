package com.example.merchant.dto;


import com.example.merchant.model.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class MerchantInputDTO {

    private String name;
    private String address;
    private String idCliente;
    private MerchantType merchantType;
}