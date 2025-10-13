package com.bankapp.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferRequest {
	@NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private Double amount; // use wrapper so @NotNull works
}
