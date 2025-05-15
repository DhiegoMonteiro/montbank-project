package com.montbank.ms.user.repositories;

import com.montbank.ms.user.models.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<CardModel, UUID> {
    List<CardModel> findAllByOwner(UUID ownerId);
}
