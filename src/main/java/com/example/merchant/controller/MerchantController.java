package com.example.merchant.controller;

import com.example.merchant.model.dto.MerchantInputDTO;
import com.example.merchant.model.dto.MerchantOutputDTO;
import com.example.merchant.entity.Merchant;
import com.example.merchant.service.MerchantService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;


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
    public ResponseEntity<MerchantOutputDTO> createMerchant(@RequestBody @Valid MerchantInputDTO merchantInputDTO) {
        MerchantOutputDTO dto = merchantService.createMerchant(merchantInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @ApiOperation(value = "Obtener merchant por ID", notes = "Devuelve un merchant por su ID. Si se pasa 'simpleOutput', la respuesta contiene solo datos básicos.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchant encontrado"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> findById(@PathVariable String id,
                                                      @RequestParam(value = "simpleOutput", required = false) String simpleOutput) {
        MerchantOutputDTO dto = merchantService.getMerchantById(id, simpleOutput);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }


    @ApiOperation(value = "Buscar merchants por nombre", notes = "Devuelve una lista de merchants que coincidan con el nombre proporcionado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchants encontrados"),
            @ApiResponse(code = 404, message = "No se encontraron merchants")
    })
    @GetMapping("/search/{name}")
    public ResponseEntity<List<MerchantOutputDTO>> findMerchantsByName(@PathVariable String name) {
        List<MerchantOutputDTO> result = merchantService.findMerchantsByName(name);
        return result.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }


    @ApiOperation(value = "Actualizar un merchant", notes = "Modifica los datos de un merchant existente por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchant actualizado correctamente"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MerchantOutputDTO> updateMerchant(@PathVariable String id,
                                                            @RequestBody @Valid MerchantInputDTO merchantInputDTO) {
        MerchantOutputDTO dto = merchantService.updateMerchant(id, merchantInputDTO);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }


    @ApiOperation(value = "Obtener el ID del cliente asociado a un merchant", notes = "Devuelve el ID del cliente al que pertenece el merchant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ID del cliente obtenido correctamente"),
            @ApiResponse(code = 204, message = "Merchant sin cliente asociado"),
            @ApiResponse(code = 404, message = "Merchant no encontrado")
    })
    @GetMapping("/{id}/client")
    public ResponseEntity<String> getClientIdByMerchant(@PathVariable String id) {
        String clientId = merchantService.getClientIdByMerchant(id);

        if (clientId == null) {
            Merchant exists = merchantService.findRawById(id);
            return exists == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(clientId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable String id) {
        boolean deleted = merchantService.deleteMerchant(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<MerchantOutputDTO>> getAllMerchants() {
        List<MerchantOutputDTO> merchants = merchantService.getAllMerchants();
        return merchants != null ? ResponseEntity.ok(merchants) : ResponseEntity.notFound().build();
    }

}