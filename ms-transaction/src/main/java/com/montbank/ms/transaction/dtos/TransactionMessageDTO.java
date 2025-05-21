package com.montbank.ms.transaction.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@AllArgsConstructor
@Getter @Setter
public class TransactionMessageDTO implements Serializable {

    private UUID sender;
    private BigDecimal amount;
    private String receiver;

}
