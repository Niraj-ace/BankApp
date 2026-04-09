package com.bankapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {

    private String accountHolderName;
    private Double balance;
    private String email;
}
