package com.montbank.ms.transaction.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter @Setter
@Table (name = "USERS_TRANSACTIONS")
public class TransactionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionID;
    private UUID sender;
    private String senderName;
    private String senderEmail;
    private BigDecimal amount;
    private String receiver;
    private String title;
    private LocalDateTime createdAt = LocalDateTime.now();;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
