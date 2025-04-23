package com.example.merchant.mappers;

import com.example.merchant.dto.MerchantOutputDTO;
import com.example.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantOutputMapper {

    MerchantOutputMapper INSTANCE = Mappers.getMapper(MerchantOutputMapper.class);

    // Mapea la entidad Merchant a MerchantDTO
    MerchantOutputDTO merchantToMerchantDTO(Merchant merchant);

    // Mapea el DTO MerchantDTO a la entidad Merchant
    Merchant merchantDTOToMerchant(MerchantOutputDTO merchantOutputDTO);
}