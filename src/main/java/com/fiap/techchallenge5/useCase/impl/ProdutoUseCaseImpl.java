package com.fiap.techchallenge5.useCase.impl;

import com.fiap.techchallenge5.domain.Ean;
import com.fiap.techchallenge5.domain.Produto;
import com.fiap.techchallenge5.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.ProdutoDTO;
import com.fiap.techchallenge5.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge5.infrastructure.repository.ProdutoRepository;
import com.fiap.techchallenge5.useCase.ProdutoUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProdutoUseCaseImpl implements ProdutoUseCase {

    private final ProdutoRepository repository;

    public ProdutoUseCaseImpl(final ProdutoRepository repository) {
        this.repository = repository;
    }


    @Override
    public boolean cadastra(final CriaProdutoDTO dadosProduto) {
        final var produto = new Produto(
                dadosProduto.ean(),
                dadosProduto.nome(),
                dadosProduto.preco(),
                dadosProduto.quantidade()
        );

        final var produtoNaBase = this.repository.findById(dadosProduto.ean());
        if(produtoNaBase.isEmpty()) {
            var produtoEntity = new ProdutoEntity(
                    produto.ean(),
                    produto.nome(),
                    produto.preco(),
                    produto.quantidade(),
                    LocalDateTime.now()
            );

            this.repository.save(produtoEntity);
            return true;
        }
        System.out.println("Produto já cadastrado");
        return false;

    }

    @Override
    public boolean atualiza(final Long ean,
                            final AtualizaProdutoDTO dadosProduto) {
        final var produto = new Produto(
                ean,
                dadosProduto.nome(),
                dadosProduto.preco(),
                dadosProduto.quantidade()
        );

        final var produtoNaBase = this.repository.findById(ean);
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return false;
        }

        var produtoEntity = new ProdutoEntity(
                produto.ean(),
                produto.nome(),
                produto.preco(),
                produto.quantidade() + produtoNaBase.get().getQuantidade(),
                LocalDateTime.now()
        );

        this.repository.save(produtoEntity);
        return true;

    }

    @Override
    public boolean deleta(final Long ean) {
        final var eanObjeto = new Ean(ean);

        final var produtoNaBase = this.repository.findById(eanObjeto.numero());
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return false;
        }
        this.repository.deleteById(eanObjeto.numero());
        return true;

    }

    @Override
    public ProdutoDTO busca(final Long ean) {
        final var eanObjeto = new Ean(ean);

        final var produtoNaBase = this.repository.findById(eanObjeto.numero());
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return null;
        }
        final var produtoEntity = produtoNaBase.get();

        return new ProdutoDTO(
                produtoEntity.getEan(),
                produtoEntity.getNome(),
                produtoEntity.getPreco(),
                produtoEntity.getQuantidade(),
                produtoEntity.getDataDeCriacao()
        );

    }

}
