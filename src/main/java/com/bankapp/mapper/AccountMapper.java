package com.bankapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.bankapp.dto.AccountResponse;
import com.bankapp.entity.Account;
import com.bankapp.entity.User;



@Mapper(componentModel = "spring")
public interface AccountMapper {
	
	@Mapping(source="user.email", target="email")
	@Mapping(source="user.username", target="name")
	@Mapping(source="user.role", target="role")
	@Mapping(source="account.id", target="id")
    AccountResponse mapAccountAndUserToAccountResponse(Account account, User user);
}

