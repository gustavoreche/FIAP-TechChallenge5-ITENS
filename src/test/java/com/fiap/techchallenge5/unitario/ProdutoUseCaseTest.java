package com.fiap.techchallenge5.unitario;

import com.fiap.techchallenge5.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge5.infrastructure.repository.ProdutoRepository;
import com.fiap.techchallenge5.useCase.impl.ProdutoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProdutoUseCaseTest {

    @Test
    public void cadastra_salvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void cadastra_produtoJaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(
                                new ProdutoEntity(
                                        7894900011517L,
                                        "Produto Teste",
                                        new BigDecimal("100"),
                                        100,
                                        LocalDateTime.now()
                                )
                        )
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @Test
    public void atualiza_salvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void atualiza_produtoNaoEstaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @Test
    public void deleta_deletaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.doNothing().when(repository).deleteById(Mockito.any());
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.deleta(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).deleteById(Mockito.any());
    }

    @Test
    public void deleta_produtoNaoEstaCadastrado_naoDeletaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.deleta(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    @Test
    public void busca_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.busca(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @Test
    public void busca_produtoNaoEstaCadastrado_naoEncontraNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução
        service.busca(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void cadastra_camposInvalidos_naoSalvaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.cadastra(
                    new CriaProdutoDTO(
                            ean,
                            nome,
                            preco,
                            quantidade
                    )
            );
        });
        verify(repository, times(0)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void atualiza_camposInvalidos_naoSalvaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.atualiza(
                    ean,
                    new AtualizaProdutoDTO(
                            nome,
                            preco,
                            quantidade
                    )
            );
        });
        verify(repository, times(0)).save(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1000,
            -1L,
            0
    })
    public void deleta_camposInvalidos_naoDeletaNaBaseDeDados(Long ean) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        var service = new ProdutoUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.deleta(
                    ean == -1000 ? null : ean
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1000,
            -1L,
            0
    })
    public void busca_camposInvalidos_naoBuscaNaBaseDeDados(Long ean) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);

        var service = new ProdutoUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.busca(
                    ean == -1000 ? null : ean
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(null, "Nome de teste", new BigDecimal("100"), 100L),
                Arguments.of(-1L, "Nome de teste", new BigDecimal("100"), 100L),
                Arguments.of(0L, "Nome de teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, null, new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, " ", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "ab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", null, 100L),
                Arguments.of(123456789L, "Nome de teste", BigDecimal.ZERO, 100L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("-1"), 100L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), null),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), -1L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), 0L),
                Arguments.of(123456789L, "Nome de teste", new BigDecimal("100"), 1001L)
        );
    }

}
