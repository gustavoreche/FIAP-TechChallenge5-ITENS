package com.fiap.techchallenge5.infrastructure.item.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public record CriaItemDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long ean,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		BigDecimal preco,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long quantidade
) {}
