package com.bankapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountReportDTO {

    private String accountNumber;
    private String accountHolder;
    private double balance;
    private String reportGeneratedOn;

}
