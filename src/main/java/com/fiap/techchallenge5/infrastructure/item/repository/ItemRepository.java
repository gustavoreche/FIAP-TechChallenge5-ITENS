package com.fiap.techchallenge5.infrastructure.item.repository;

import com.fiap.techchallenge5.infrastructure.item.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
