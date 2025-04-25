package com.example.merchant.service;

import com.example.merchant.entity.Merchant;
import com.example.merchant.mappers.MerchantInputMapper;
import com.example.merchant.mappers.MerchantOutputMapper;
import com.example.merchant.model.dto.MerchantInputDTO;
import com.example.merchant.model.dto.MerchantOutputDTO;
import com.example.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantOutputDTO createMerchant(MerchantInputDTO dto) {
        Merchant merchant = MerchantInputMapper.INSTANCE.merchantInputDTOToMerchant(dto);
        merchant.setId(UUID.randomUUID().toString());
        merchant.setPk("merchantEntity");
        merchant.setSk("documentID#" + merchant.getName());
        merchant.setNameLowerCase(merchant.getName().toLowerCase());  // Guardar el nombre en minúsculas
        return MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(merchantRepository.save(merchant));
    }

    public MerchantOutputDTO getMerchantById(String id, String simpleOutput) {
        Merchant merchant = merchantRepository.findById(id);
        if (merchant == null) return null;

        if ("simpleOutput".equals(simpleOutput)) {
            return new MerchantOutputDTO(merchant.getId(), merchant.getName(), null, null, null);
        }

        return MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(merchant);
    }

    public List<MerchantOutputDTO> findMerchantsByName(String name) {
        return merchantRepository.findByName(name)
                .stream()
                .map(MerchantOutputMapper.INSTANCE::merchantToMerchantDTO)
                .collect(Collectors.toList());
    }

    public MerchantOutputDTO updateMerchant(String id, MerchantInputDTO dto) {
        Merchant merchant = merchantRepository.findById(id);
        if (merchant == null) return null;

        merchant.setName(dto.getName());
        merchant.setAddress(dto.getAddress());
        merchant.setMerchantType(dto.getMerchantType());
        merchant.setIdCliente(dto.getIdCliente());
        merchant.setNameLowerCase(dto.getName().toLowerCase());  // Guardar el nombre en minúsculas

        return MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(merchantRepository.save(merchant));
    }

    public String getClientIdByMerchant(String id) {
        Merchant merchant = merchantRepository.findById(id);
        return merchant != null ? merchant.getIdCliente() : null;
    }

    public Merchant findRawById(String id) {
        return merchantRepository.findById(id);
    }
}