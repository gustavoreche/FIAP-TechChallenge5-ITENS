package com.fiap.techchallenge5.infrastructure.item.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemDTO(

		Long ean,
		String nome,
		BigDecimal preco,
		Long quantidade,
		LocalDateTime dataDeCriacao
) {}
