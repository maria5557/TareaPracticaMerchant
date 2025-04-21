package com.example.merchant.service;

import com.example.merchant.entity.Merchant;
import com.example.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public Merchant createMerchant(Merchant merchant) {
        merchant.setPk("merchantEntity");
        merchant.setSk("documentID#" + merchant.getName());
        merchant.setId(UUID.randomUUID().toString());
        return merchantRepository.save(merchant);
    }

    public Merchant saveMerchant(Merchant merchant) {
        merchant.setPk("merchantEntity");
        merchant.setSk("documentID#" + merchant.getName());
        return merchantRepository.save(merchant);
    }

    public Merchant getMerchantById(String id) {
        return merchantRepository.findById(id);
    }

    public List<Merchant> findMerchantsByName(String name) {
        return merchantRepository.findByName(name);
    }
}