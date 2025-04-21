package com.example.merchant.mappers;

import com.example.merchant.dto.MerchantDTO;
import com.example.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantMapper {

    MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

    // Mapea la entidad Merchant a MerchantDTO
    MerchantDTO merchantToMerchantDTO(Merchant merchant);

    // Mapea el DTO MerchantDTO a la entidad Merchant
    Merchant merchantDTOToMerchant(MerchantDTO merchantDTO);
}