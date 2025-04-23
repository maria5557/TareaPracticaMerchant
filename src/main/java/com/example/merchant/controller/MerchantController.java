package com.example.merchant.controller;

import com.example.merchant.dto.MerchantInputDTO;
import com.example.merchant.dto.MerchantOutputDTO;
import com.example.merchant.entity.Merchant;
import com.example.merchant.mappers.MerchantInputMapper;
import com.example.merchant.mappers.MerchantOutputMapper;
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
    public ResponseEntity<MerchantOutputDTO> createMerchant(@RequestBody @Valid MerchantInputDTO merchantInputDTO) {
        Merchant saved = merchantService.createMerchant(MerchantInputMapper.INSTANCE.merchantInputDTOToMerchant(merchantInputDTO));
        return ResponseEntity.ok(MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(saved));
    }

    // Buscar merchant por ID, con opción simpleOutput
    @GetMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> findById(
            @PathVariable String id,
            @RequestParam(value = "simpleOutput", required = false) String simpleOutput) {

        Merchant merchant = merchantService.getMerchantById(id);
        System.out.println("Valor de simpleOutput: " + simpleOutput);

        if (merchant != null) {
            if (StringUtils.equals("simpleOutput", simpleOutput)) {
                return ResponseEntity.ok(new MerchantOutputDTO(merchant.getId(), merchant.getName(), null, null,null));
            } else {
                return ResponseEntity.ok(MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(merchant));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Buscar merchants por nombre
    @GetMapping("/search/{name}")
    public ResponseEntity<List<MerchantOutputDTO>> findMerchantsByName(@PathVariable String name) {
        List<Merchant> merchants = merchantService.findMerchantsByName(name);

        if (merchants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MerchantOutputDTO> merchantOutputDTOS = merchants.stream()
                .map(MerchantOutputMapper.INSTANCE::merchantToMerchantDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(merchantOutputDTOS);
    }

    // Actualizar merchant
    @PutMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> updateMerchant(
            @PathVariable String id,
            @RequestBody @Valid MerchantInputDTO merchantInputDTO) {

        Merchant existing = merchantService.getMerchantById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        existing.setName(merchantInputDTO.getName());
        existing.setAddress(merchantInputDTO.getAddress());
        existing.setMerchantType(merchantInputDTO.getMerchantType());
        existing.setIdCliente(merchantInputDTO.getIdCliente());

        Merchant updated = merchantService.saveMerchant(existing);

        return ResponseEntity.ok(MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(updated));
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