package org.eventbook.eventbooking.web.mapper;

import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.web.dto.auth.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(source = "id", target = "", ignore = true)
    List<User> toEntity(List<UserDto> dtos);
}
