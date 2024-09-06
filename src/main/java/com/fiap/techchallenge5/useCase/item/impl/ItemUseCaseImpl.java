package com.fiap.techchallenge5.useCase.item.impl;

import com.fiap.techchallenge5.domain.Ean;
import com.fiap.techchallenge5.domain.Item;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.AtualizaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.ItemDTO;
import com.fiap.techchallenge5.infrastructure.item.model.ItemEntity;
import com.fiap.techchallenge5.infrastructure.item.repository.ItemRepository;
import com.fiap.techchallenge5.infrastructure.usuario.client.UsuarioClient;
import com.fiap.techchallenge5.useCase.item.ItemUseCase;
import com.fiap.techchallenge5.useCase.token.TokenUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class ItemUseCaseImpl implements ItemUseCase {

    private final ItemRepository repository;
    private final TokenUseCase serviceToken;
    private final UsuarioClient clientUsuario;

    public ItemUseCaseImpl(final ItemRepository repository,
                           final TokenUseCase serviceToken,
                           final UsuarioClient clientUsuario) {
        this.repository = repository;
        this.serviceToken = serviceToken;
        this.clientUsuario = clientUsuario;
    }


    @Override
    public boolean cadastra(final CriaItemDTO dadosItem,
                            final String token) {
        final var item = new Item(
                dadosItem.ean(),
                dadosItem.nome(),
                dadosItem.preco(),
                dadosItem.quantidade()
        );

        final var usuario = this.pegaUsuario(token);
        if(Objects.isNull(usuario)) {
            return false;
        }

        final var itemNaBase = this.repository.findById(dadosItem.ean());
        if(itemNaBase.isEmpty()) {
            var itemEntity = new ItemEntity(
                    item.ean(),
                    item.nome(),
                    item.preco(),
                    item.quantidade(),
                    LocalDateTime.now()
            );

            this.repository.save(itemEntity);
            return true;
        }
        System.out.println("Item já cadastrado");
        return false;

    }

    @Override
    public boolean atualiza(final Long ean,
                            final AtualizaItemDTO dadosItem,
                            final String token) {
        final var item = new Item(
                ean,
                dadosItem.nome(),
                dadosItem.preco(),
                dadosItem.quantidade()
        );

        final var usuario = this.pegaUsuario(token);
        if(Objects.isNull(usuario)) {
            return false;
        }

        final var itemNaBase = this.repository.findById(ean);
        if(itemNaBase.isEmpty()) {
            System.out.println("Item não está cadastrado");
            return false;
        }

        var itemEntity = new ItemEntity(
                item.ean(),
                item.nome(),
                item.preco(),
                item.quantidade() + itemNaBase.get().getQuantidade(),
                LocalDateTime.now()
        );

        this.repository.save(itemEntity);
        return true;

    }

    @Override
    public boolean deleta(final Long ean,
                          final String token) {
        final var eanObjeto = new Ean(ean);

        final var usuario = this.pegaUsuario(token);
        if(Objects.isNull(usuario)) {
            return false;
        }

        final var itemNaBase = this.repository.findById(eanObjeto.numero());
        if(itemNaBase.isEmpty()) {
            System.out.println("Item não está cadastrado");
            return false;
        }
        this.repository.deleteById(eanObjeto.numero());
        return true;

    }

    @Override
    public ItemDTO busca(final Long ean,
                         final String token) {
        final var eanObjeto = new Ean(ean);

        final var usuario = this.pegaUsuario(token);
        if(Objects.isNull(usuario)) {
            return null;
        }

        final var itemNaBase = this.repository.findById(eanObjeto.numero());
        if(itemNaBase.isEmpty()) {
            System.out.println("Item não está cadastrado");
            return null;
        }
        final var itemEntity = itemNaBase.get();

        return new ItemDTO(
                itemEntity.getEan(),
                itemEntity.getNome(),
                itemEntity.getPreco(),
                itemEntity.getQuantidade(),
                itemEntity.getDataDeCriacao()
        );

    }

    private String pegaUsuario(final String token) {
        final var jwt = this.serviceToken.pegaJwt(token.replace("Bearer ", ""));
        if(Objects.isNull(jwt)){
            log.error("Token inválido");
            return null;
        }

        final var usuario = this.serviceToken.pegaUsuario(jwt);
        try {
            final var usuarioExiste = this.clientUsuario.usuarioExiste(usuario, token);
            if(Objects.isNull(usuarioExiste) || !usuarioExiste) {
                log.error("Usuario não encontrado");
                return null;
            }
        } catch (Exception e) {
            log.error("Erro ao buscar usuário ", e);
            return null;
        }
        return usuario;
    }

}
