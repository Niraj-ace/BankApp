package com.bankapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	
	@Mapping(source = "name", target = "username")
	User mapUserRequestDTOToUser(UserRequestDTO request);
	
	UserResponseDTO mapUserToUserResponsetDTO(User user);

}
