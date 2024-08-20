package com.fiap.techchallenge5.useCase;

import com.fiap.techchallenge5.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.ProdutoDTO;

public interface ProdutoUseCase {

    boolean cadastra(final CriaProdutoDTO produto);

    boolean atualiza(final Long ean,
                     final AtualizaProdutoDTO dadosProduto);

    boolean deleta(final Long ean);

    ProdutoDTO busca(final Long ean);

}
