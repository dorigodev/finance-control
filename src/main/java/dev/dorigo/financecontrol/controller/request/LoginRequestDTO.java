package dev.dorigo.financecontrol.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
        @Schema(type = "string", description = "email para o login")
        String email,
        @Schema(type = "string", description = "senha para o login")
        String password) {
}
