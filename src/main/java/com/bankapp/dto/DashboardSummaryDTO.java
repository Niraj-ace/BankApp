package com.bankapp.dto;

import com.bankapp.entity.Category;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {

    private double totalDeposits;
    private double totalWithdrawals;
    private double currentBalance;
    private Category topCategory;

}
