package com.fiap.techchallenge5.useCase.item;

import com.fiap.techchallenge5.infrastructure.item.controller.dto.AtualizaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.ItemDTO;

public interface ItemUseCase {

    boolean cadastra(final CriaItemDTO item,
                     final String token);

    boolean atualiza(final Long ean,
                     final AtualizaItemDTO dadosItem,
                     final String token);

    boolean deleta(final Long ean,
                   final String token);

    ItemDTO busca(final Long ean,
                  final String token);

}
