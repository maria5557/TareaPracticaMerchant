package com.example.merchant.mappers;

import com.example.merchant.model.dto.MerchantOutputDTO;
import com.example.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantOutputMapper {

    MerchantOutputMapper INSTANCE = Mappers.getMapper(MerchantOutputMapper.class);

    // Mapea la entidad Merchant a MerchantDTO
    @Mapping(target = "merchantTypeDescription", expression = "java(merchant.getMerchantType().getDisplayName())")
    MerchantOutputDTO merchantToMerchantDTO(Merchant merchant);

}