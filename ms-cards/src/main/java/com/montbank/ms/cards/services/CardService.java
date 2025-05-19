package com.montbank.ms.cards.services;

import com.montbank.ms.cards.dtos.CardDTO;
import com.montbank.ms.cards.models.CardModel;
import com.montbank.ms.cards.repositories.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    public List<CardModel> getCardsByOwner(UUID ownerId) {
        return cardRepository.findAllByOwner(ownerId);
    }

    @Transactional
    public CardModel generateCard(CardDTO cardDTO, UUID ownerId, String ownerName) {
        CardModel card = new CardModel();
        card.setOwner(ownerId);
        card.setCardName(cardDTO.cardName());
        card.setOwnerName(ownerName);
        card.setType(cardDTO.type());
        card.setNumber(generateCardNumber());
        card.setExpireDate(generateExpireDate());
        card.setCvv(generateCvv());
        cardRepository.save(card);
        return card;
    }
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(random.nextInt(10));
            if (i == 3 || i == 7 || i == 11) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    private LocalDate generateExpireDate() {
        return LocalDate.now().plusYears(2)
                .withMonth(12)
                .withDayOfMonth(31);
    }
    private String generateCvv() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }

    @Transactional
    public void deleteCard(UUID userId, UUID cardId){
        CardModel card = cardRepository.findById(cardId).orElseThrow(()
                -> new EntityNotFoundException("Cartão não encontrado"));
        if(card.getOwner().equals(userId)){
            cardRepository.deleteById(cardId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cartão não pertence ao usuário");
        }
    }
    @Transactional
    public void updateCard(CardDTO cardDTO, UUID userId, UUID cardId){
        CardModel card = cardRepository.findById(cardId).orElseThrow(()
                -> new EntityNotFoundException("Cartão não encontrado"));
        if(card.getOwner().equals(userId)){
            BeanUtils.copyProperties(cardDTO, card);
            cardRepository.save(card);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cartão não pertence ao usuário");
        }
    }
}

