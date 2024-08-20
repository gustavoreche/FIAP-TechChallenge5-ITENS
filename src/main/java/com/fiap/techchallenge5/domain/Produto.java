package com.fiap.techchallenge5.domain;

import java.math.BigDecimal;
import java.util.Objects;


public record Produto (
    Long ean,
    String nome,
    BigDecimal preco,
    long quantidade
) {

        public Produto {
            if (Objects.isNull(nome) || nome.isEmpty()) {
                throw new IllegalArgumentException("NOME NAO PODE SER NULO OU VAZIO!");
            }
            if (nome.length() < 3 || nome.length() > 50) {
                throw new IllegalArgumentException("O NOME deve ter no mínimo 3 letras e no máximo 50 letras");
            }

            if (Objects.isNull(preco) || preco.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("PRECO NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
            }

            if (Objects.isNull(quantidade) || (quantidade <= 0 || quantidade > 1000)) {
                throw new IllegalArgumentException("QUANTIDADE NAO PODE SER NULO OU MENOR E IGUAL A ZERO E MAIOR QUE 1000!");
            }

            ean = new Ean(ean).numero();
        }
}
