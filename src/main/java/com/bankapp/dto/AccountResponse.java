package com.bankapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {
	
	private String accountNumber;
	private String accountHolderName;
    private Double balance;
    private String email;
    private String name;
    private String role;
    
}
