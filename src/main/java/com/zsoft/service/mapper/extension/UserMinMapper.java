package com.zsoft.service.mapper.extension;

import com.zsoft.domain.User;
import com.zsoft.service.dto.extension.UserMinDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMinMapper {

    default UserMinDTO toDto(User user){
        UserMinDTO dto = new UserMinDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setImageUrl(user.getImageUrl());
        return dto;
    }

    List<UserMinDTO> usersToUserMinDTOs(List<User> users);

    default User toEntity(UserMinDTO dto){
        if (dto == null) {
            return null;
        } else {
            User user = new User();
            user.setId(dto.getId());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setImageUrl(dto.getImageUrl());
            return user;
        }
    }

    List<User> userMinDTOsToUsers(List<UserMinDTO> dtos);
}
