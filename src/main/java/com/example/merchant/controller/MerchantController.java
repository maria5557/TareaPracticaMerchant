package com.example.merchant.controller;

import com.example.merchant.dto.MerchantInputDTO;
import com.example.merchant.dto.MerchantOutputDTO;
import com.example.merchant.entity.Merchant;
import com.example.merchant.mappers.MerchantInputMapper;
import com.example.merchant.mappers.MerchantOutputMapper;
import com.example.merchant.service.MerchantService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Controlador de merchants", tags = "Merchants")
@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Validated
public class MerchantController {

    private final MerchantService merchantService;

    @ApiOperation(value = "Crear un nuevo merchant", notes = "Registra un nuevo merchant en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Merchant creado correctamente"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MerchantOutputDTO> createMerchant(
            @ApiParam(value = "Datos del merchant", required = true)
            @RequestBody @Valid MerchantInputDTO merchantInputDTO) {

        Merchant saved = merchantService.createMerchant(
                MerchantInputMapper.INSTANCE.merchantInputDTOToMerchant(merchantInputDTO));

        return ResponseEntity.ok(MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(saved));
    }

    @ApiOperation(value = "Obtener merchant por ID", notes = "Devuelve un merchant por su ID. Si se pasa 'simpleOutput', la respuesta contiene solo datos básicos.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchant encontrado"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> findById(
            @ApiParam(value = "ID del merchant", required = true) @PathVariable String id,
            @ApiParam(value = "Si se pasa 'simpleOutput', devuelve solo nombre e ID")
            @RequestParam(value = "simpleOutput", required = false) String simpleOutput) {

        Merchant merchant = merchantService.getMerchantById(id);

        if (merchant != null) {
            if (StringUtils.equals("simpleOutput", simpleOutput)) {
                return ResponseEntity.ok(new MerchantOutputDTO(merchant.getId(), merchant.getName(), null, null, null));
            } else {
                return ResponseEntity.ok(MerchantOutputMapper.INSTANCE.merchantToMerchantDTO(merchant));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ApiOperation(value = "Buscar merchants por nombre", notes = "Devuelve una lista de merchants que coincidan con el nombre proporcionado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchants encontrados"),
            @ApiResponse(code = 404, message = "No se encontraron merchants")
    })
    @GetMapping("/search/{name}")
    public ResponseEntity<List<MerchantOutputDTO>> findMerchantsByName(
            @ApiParam(value = "Nombre del merchant a buscar", required = true)
            @PathVariable String name) {

        List<Merchant> merchants = merchantService.findMerchantsByName(name);

        if (merchants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MerchantOutputDTO> merchantOutputDTOS = merchants.stream()
                .map(MerchantOutputMapper.INSTANCE::merchantToMerchantDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(merchantOutputDTOS);
    }

    @ApiOperation(value = "Actualizar un merchant", notes = "Modifica los datos de un merchant existente por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchant actualizado correctamente"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> updateMerchant(
            @ApiParam(value = "ID del merchant", required = true) @PathVariable String id,
            @ApiParam(value = "Datos actualizados del merchant", required = true) @RequestBody @Valid MerchantInputDTO merchantInputDTO) {

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

    @ApiOperation(value = "Obtener el ID del cliente asociado a un merchant", notes = "Devuelve el ID del cliente al que pertenece el merchant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ID del cliente obtenido correctamente"),
            @ApiResponse(code = 204, message = "Merchant sin cliente asociado"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @GetMapping("/{id}/client")
    public ResponseEntity<String> getClientIdByMerchant(
            @ApiParam(value = "ID del merchant", required = true) @PathVariable String id) {

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