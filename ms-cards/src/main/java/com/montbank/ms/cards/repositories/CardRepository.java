package com.montbank.ms.cards.repositories;

import com.montbank.ms.cards.models.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<CardModel, UUID> {
    List<CardModel> findAllByOwner(UUID ownerId);
}
