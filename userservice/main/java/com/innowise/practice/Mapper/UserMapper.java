package com.innowise.practice.Mapper;

import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.DBSchema.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(source = "birth_date", target = "birth_date")
    Users toUser(UserRequest userRequest);
    UserResponse toUserResponse(Users user);
}
