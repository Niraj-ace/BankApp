package com.bankapp.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	
	@Mapping(source = "name", target = "username")
	User mapUserRequestDTOToUser(UserRequestDTO request);
	
	UserResponseDTO mapUserToUserResponsetDTO(User user);
	
	default List<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return null;
        }

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
