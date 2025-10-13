package com.bankapp.dto;

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
    private String topCategory;

}
