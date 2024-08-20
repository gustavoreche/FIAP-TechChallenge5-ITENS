package com.fiap.techchallenge5.unitario;

import com.fiap.techchallenge5.infrastructure.item.controller.dto.AtualizaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.model.ItemEntity;
import com.fiap.techchallenge5.infrastructure.item.repository.ItemRepository;
import com.fiap.techchallenge5.useCase.item.impl.ItemUseCaseImpl;
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

public class ItemUseCaseTest {

    @Test
    public void cadastra_salvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.cadastra(
                new CriaItemDTO(
                        7894900011517L,
                        "Item Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void cadastra_itemJaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(
                                new ItemEntity(
                                        7894900011517L,
                                        "Item Teste",
                                        new BigDecimal("100"),
                                        100,
                                        LocalDateTime.now()
                                )
                        )
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.cadastra(
                new CriaItemDTO(
                        7894900011517L,
                        "Item Teste",
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
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaItemDTO(
                        "Item Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void atualiza_itemNaoEstaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaItemDTO(
                        "Item Teste",
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
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.doNothing().when(repository).deleteById(Mockito.any());
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.deleta(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).deleteById(Mockito.any());
    }

    @Test
    public void deleta_itemNaoEstaCadastrado_naoDeletaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ItemUseCaseImpl(repository);

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
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ItemUseCaseImpl(repository);

        // execução
        service.busca(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @Test
    public void busca_itemNaoEstaCadastrado_naoEncontraNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ItemUseCaseImpl(repository);

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
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ItemUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.cadastra(
                    new CriaItemDTO(
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
        var repository = Mockito.mock(ItemRepository.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ItemEntity(
                                7894900011517L,
                                "Item Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ItemUseCaseImpl(repository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.atualiza(
                    ean,
                    new AtualizaItemDTO(
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
        var repository = Mockito.mock(ItemRepository.class);

        var service = new ItemUseCaseImpl(repository);

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
        var repository = Mockito.mock(ItemRepository.class);

        var service = new ItemUseCaseImpl(repository);

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
