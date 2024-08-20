package com.fiap.techchallenge5.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDTO(

		Long ean,
		String nome,
		BigDecimal preco,
		Long quantidade,
		LocalDateTime dataDeCriacao
) {}
