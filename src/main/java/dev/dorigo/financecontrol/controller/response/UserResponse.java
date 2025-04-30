package dev.dorigo.financecontrol.controller.response;

import dev.dorigo.financecontrol.domain.user.UserRole;
import lombok.Builder;

@Builder
public record UserResponse(Long id,
                           String name,
                           String email
                          ) {
}
