package com.example.merchant.mappers;

import com.example.merchant.model.dto.MerchantInputDTO;
import com.example.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantInputMapper {

    MerchantInputMapper INSTANCE = Mappers.getMapper(MerchantInputMapper.class);

    Merchant merchantInputDTOToMerchant(MerchantInputDTO dto);
}