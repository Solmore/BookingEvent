package org.eventbook.eventbooking.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.AuthService;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.eventbook.eventbooking.web.dto.auth.UserDto;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.mapper.UserMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users")
public class UserController {

    private final UserMapper userMapper;

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/auth")
    public CredentialsResponse auth(@Validated(OnCreate.class)
                             @RequestBody final Credentials authRequest) {
        return authService.auth(authRequest);
    }

    @PostMapping("/users")
    public void create(@Validated(OnCreate.class)
                       @RequestBody final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userService.create(user);

    }

    @GetMapping("/users/{userId}/view")
    @SecurityRequirements({
            @SecurityRequirement(name = "BasicAuthentication"),
            @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public List<Event> findAllByUserId(@PathVariable
                                         final BigInteger userId) {
        return userService.getAllByUserId(userId);
    }

}
