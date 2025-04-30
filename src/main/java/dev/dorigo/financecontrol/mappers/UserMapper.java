package dev.dorigo.financecontrol.mappers;

import dev.dorigo.financecontrol.controller.request.UserRequest;
import dev.dorigo.financecontrol.controller.response.UserResponse;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.domain.user.UserRole;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static User toUser(UserRequest request){
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(UserRole.USER)
                .active(true)
                .build();
    }

    public static UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
