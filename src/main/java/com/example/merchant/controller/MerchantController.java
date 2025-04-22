package com.example.merchant.controller;

import com.example.merchant.dto.MerchantFullDTO;
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
    public ResponseEntity<MerchantFullDTO> createMerchant(@RequestBody @Valid MerchantFullDTO merchantFullDTO) {
        Merchant saved = merchantService.createMerchant(MerchantMapper.INSTANCE.merchantDTOToMerchant(merchantFullDTO));
        return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(saved));
    }

    // Buscar merchant por ID, con opción simpleOutput
    @GetMapping("/{id}")
    public ResponseEntity<MerchantFullDTO> findById(
            @PathVariable String id,
            @RequestParam(value = "simpleOutput", required = false) String simpleOutput) {

        Merchant merchant = merchantService.getMerchantById(id);
        System.out.println("Valor de simpleOutput: " + simpleOutput);

        if (merchant != null) {
            if (StringUtils.equals("simpleOutput", simpleOutput)) {
                return ResponseEntity.ok(new MerchantFullDTO(merchant.getId(), merchant.getName(), null, null,null));
            } else {
                return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(merchant));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Buscar merchants por nombre
    @GetMapping("/search/{name}")
    public ResponseEntity<List<MerchantFullDTO>> findMerchantsByName(@PathVariable String name) {
        List<Merchant> merchants = merchantService.findMerchantsByName(name);

        if (merchants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MerchantFullDTO> merchantFullDTOS = merchants.stream()
                .map(MerchantMapper.INSTANCE::merchantToMerchantDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(merchantFullDTOS);
    }

    // Actualizar merchant
    @PutMapping("/{id}")
    public ResponseEntity<MerchantFullDTO> updateMerchant(
            @PathVariable String id,
            @RequestBody @Valid MerchantFullDTO merchantFullDTO) {

        Merchant existing = merchantService.getMerchantById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        existing.setName(merchantFullDTO.getName());
        existing.setAddress(merchantFullDTO.getAddress());
        existing.setMerchantType(merchantFullDTO.getMerchantType());
        existing.setIdCliente(merchantFullDTO.getIdCliente());

        Merchant updated = merchantService.saveMerchant(existing);

        return ResponseEntity.ok(MerchantMapper.INSTANCE.merchantToMerchantDTO(updated));
    }

    // Obtener el ID del cliente al que pertenece un merchant
    @GetMapping("/{id}/client")
    public ResponseEntity<String> getClientIdByMerchant(@PathVariable String id) {
        Merchant merchant = merchantService.getMerchantById(id);

        if (merchant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (merchant.getIdCliente() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(merchant.getIdCliente());
    }
}