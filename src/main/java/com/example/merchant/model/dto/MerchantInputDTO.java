package com.example.merchant.model.dto;


import com.example.merchant.model.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO para la creación o actualización de un merchant")
@Data
@AllArgsConstructor
@Getter
@Setter
public class MerchantInputDTO {

    @ApiModelProperty(value = "Nombre del merchant")
    private String name;

    @ApiModelProperty(value = "Dirección del merchant")
    private String address;

    @ApiModelProperty(value = "ID del cliente asociado (puede ser null en creación)")
    private String idCliente;

    @ApiModelProperty(value = "Tipo de merchant")
    private MerchantType merchantType;
}