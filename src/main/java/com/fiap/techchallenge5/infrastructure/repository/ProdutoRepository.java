package com.fiap.techchallenge5.infrastructure.repository;

import com.fiap.techchallenge5.infrastructure.model.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
}
