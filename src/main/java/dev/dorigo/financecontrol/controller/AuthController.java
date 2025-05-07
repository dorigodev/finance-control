package dev.dorigo.financecontrol.controller;

import dev.dorigo.financecontrol.controller.request.LoginRequestDTO;
import dev.dorigo.financecontrol.controller.request.UserRequest;
import dev.dorigo.financecontrol.controller.response.LoginResponseDTO;
import dev.dorigo.financecontrol.controller.response.UserResponse;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.infra.security.TokenService;
import dev.dorigo.financecontrol.mappers.UserMapper;
import dev.dorigo.financecontrol.repository.UserRepository;
import dev.dorigo.financecontrol.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Auth Controller", description = "Recurso responsavel pelo gerenciamento de login e registro de um usuário.")
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "Realizar o login", description = "Método responsável por realizar o login do usuário")
    @ApiResponse(responseCode = "201", description = "Login realizado com sucesso",
    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
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

    @Operation(summary = "Realizar o registro", description = "Método responsável por realizar o registro de um novo usuário")
    @ApiResponse(responseCode = "201", description = "Registro realizado com sucesso",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request){
        if(this.userRepository.findByEmail(request.email()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.save(UserMapper.toUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(savedUser));
    }
}
