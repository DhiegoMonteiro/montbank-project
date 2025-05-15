package com.montbank.ms.user.controllers;

import com.montbank.ms.user.dtos.CardDTO;
import com.montbank.ms.user.models.CardModel;
import com.montbank.ms.user.services.CardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping("/my-cards")
    public List<CardModel> getUserCards(@RequestAttribute String userId) {
        if (userId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return cardService.getCardsByOwner(UUID.fromString(userId));
    }
    @PostMapping("/my-cards/create")
    public ResponseEntity<CardModel> newCard(@RequestBody @Valid CardDTO cardDTO,
                                                     @RequestAttribute String userId,
                                             @RequestAttribute String name) {
        if (userId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.generateCard(cardDTO,
                UUID.fromString(userId), name));
    }
    @DeleteMapping("/my-cards/delete/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID cardId,
                           @RequestAttribute String userId){
        cardService.deleteCard(UUID.fromString(userId), cardId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/my-cards/edit/{cardId}")
    public ResponseEntity<Void> updateCard(@RequestBody @Valid CardDTO cardDTO,
                                           @PathVariable UUID cardId,
                                           @RequestAttribute String userId){
        cardService.updateCard(cardDTO,UUID.fromString(userId),cardId);
        return ResponseEntity.noContent().build();
    }
}
