package dev.dorigo.financecontrol.controller;

import dev.dorigo.financecontrol.controller.request.LoginRequestDTO;
import dev.dorigo.financecontrol.controller.response.LoginResponseDTO;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        try{
            var userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            var authentication = authenticationManager.authenticate(userAndPass);
            var user = (User) authentication.getPrincipal();
            var token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(e.getMessage()));
        }
    }
}
