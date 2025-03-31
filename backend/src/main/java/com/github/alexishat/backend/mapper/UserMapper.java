package com.github.alexishat.backend.mapper;

import com.github.alexishat.backend.dtos.RegisterDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User registerToUser(RegisterDto registerDto);
}


