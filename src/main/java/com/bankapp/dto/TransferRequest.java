package com.bankapp.dto;


import com.bankapp.entity.Category;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount; 
    private Category category;
}
