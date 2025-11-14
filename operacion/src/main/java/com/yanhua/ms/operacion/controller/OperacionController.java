package com.yanhua.ms.operacion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yanhua.ms.operacion.constans.ApiRoutes;
import com.yanhua.ms.operacion.dto.OperacionRequest;
import com.yanhua.ms.operacion.model.OperacionModel;
import com.yanhua.ms.operacion.service.IOperacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Operaciones", description = "Operaciones relacionadas con transacciones")
@RestController
@RequestMapping(ApiRoutes.Operacion.BASE)
public class OperacionController {

        private static final Logger logger = LoggerFactory.getLogger(OperacionController.class);

        @Autowired
        IOperacionService operacionService;

        @Operation(summary = "Registrar operación", description = "Registra una nueva operación para un cliente.")
        @ApiResponses({
                @ApiResponse(responseCode = "201", description = "Operación creada", content = @Content(schema = @Schema(implementation = OperacionModel.class))),
                @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = com.yanhua.ms.operacion.exception.ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = com.yanhua.ms.operacion.exception.ErrorResponse.class)))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear la operación", required = true, content = @Content(schema = @Schema(implementation = OperacionRequest.class)))
        @PostMapping(ApiRoutes.Operacion.REGISTRAR)
        public ResponseEntity<?> RegistrarOperacion(@RequestBody OperacionRequest request) {
                logger.info("Inicio registro de operación: idCliente={}, tipo={}, total={}",
                                request.getIdCliente(), request.getTipoOperacion(), request.getTotal());

                OperacionModel model = new OperacionModel(request.getIdCliente(), request.getTipoOperacion(),
                                request.getTotal());
                model = operacionService.Registrar(model);
                return new ResponseEntity<>(model, HttpStatus.CREATED);
        }
    
        @Operation(summary = "Listar operaciones", description = "Devuelve la lista de operaciones registradas")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de operaciones", content = @Content(schema = @Schema(implementation = OperacionModel.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = com.yanhua.ms.operacion.exception.ErrorResponse.class)))
        })
        @GetMapping(ApiRoutes.Operacion.LISTAR)
        public ResponseEntity<List<OperacionModel>> ListarOperacion() {
                logger.info("Solicitud de listado de operaciones");
                List<OperacionModel> result = operacionService.Listar();
                return new ResponseEntity<>(result, HttpStatus.OK);
        }

}
