package com.montbank.ms.user.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter @Setter
@Table (name = "users_transactions")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionID;
    private UUID sender;
    private BigDecimal amount;
    private UUID receiver;
}
