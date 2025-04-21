package com.example.merchant.controller;

import com.example.merchant.dto.MerchantDTO;
import com.example.merchant.entity.Merchant;
import com.example.merchant.mappers.MerchantMapper;
import com.example.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Validated
public class MerchantController {

    private final MerchantService merchantService;

    // Crear un merchant
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MerchantDTO> createMerchant(@RequestBody @Valid MerchantDTO merchantDTO) {
        Merchant saved = merchantService.createMerchant(MerchantMapper.INSTANCE.merchantDTOToMerchant(merchantDTO));
        return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(saved));
    }

    // Buscar merchant por ID, con opción simpleOutput
    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> findById(
            @PathVariable String id,
            @RequestParam(value = "simpleOutput", required = false) String simpleOutput) {

        Merchant merchant = merchantService.getMerchantById(id);
        System.out.println("Valor de simpleOutput: " + simpleOutput);

        if (merchant != null) {
            if (StringUtils.equals("simpleOutput", simpleOutput)) {
                return ResponseEntity.ok(new MerchantDTO(merchant.getId(), merchant.getName(), null, null));
            } else {
                return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(merchant));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Buscar merchants por nombre
    @GetMapping("/search/{name}")
    public ResponseEntity<List<MerchantDTO>> findMerchantsByName(@PathVariable String name) {
        List<Merchant> merchants = merchantService.findMerchantsByName(name);

        if (merchants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MerchantDTO> merchantDTOs = merchants.stream()
                .map(MerchantMapper.INSTANCE::merchantToMerchantDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(merchantDTOs);
    }

    // Actualizar merchant
    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(
            @PathVariable String id,
            @RequestBody @Valid MerchantDTO merchantDTO) {

        Merchant existing = merchantService.getMerchantById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        existing.setName(merchantDTO.getName());
        existing.setAddress(merchantDTO.getAddress());
        existing.setMerchantType(merchantDTO.getMerchantType());

        Merchant updated = merchantService.saveMerchant(existing);

        return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(updated));
    }
}