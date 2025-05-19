package com.montbank.ms.cards.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "TB_CARDS")
public class CardModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cardId;
    private String cardName;
    private String number;
    private LocalDate expireDate;
    private String cvv;
    private String type;
    private String ownerName;
    private UUID owner;
}
