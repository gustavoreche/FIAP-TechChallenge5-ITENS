package com.fiap.techchallenge5.infrastructure.controller;

import com.fiap.techchallenge5.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.ProdutoDTO;
import com.fiap.techchallenge5.useCase.ProdutoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(
		name = "Produtos",
		description = "Serviço para realizar o gerenciamento de produtos no sistema"
)
@RestController
@RequestMapping(ProdutoController.URL_PRODUTO)
public class ProdutoController {

	public static final String URL_PRODUTO = "/produto";
	public static final String URL_PRODUTO_COM_EAN = URL_PRODUTO + "/{ean}";

	private final ProdutoUseCase service;

	public ProdutoController(final ProdutoUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para cadastrar um produto"
	)
	@PostMapping
	public ResponseEntity<Void> cadastra(@RequestBody @Valid final CriaProdutoDTO dadosProduto) {
		final var cadastrou = this.service.cadastra(dadosProduto);
		if(cadastrou) {
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.build();
		}
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.build();
	}

	@Operation(
			summary = "Serviço para atualizar um produto"
	)
	@PutMapping("/{ean}")
	public ResponseEntity<Void> atualiza(@PathVariable("ean") final Long ean,
										 @RequestBody @Valid final AtualizaProdutoDTO dadosProduto) {
		final var atualizou = this.service.atualiza(ean, dadosProduto);
		if(atualizou) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.build();
		}
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

	@Operation(
			summary = "Serviço para deletar um produto"
	)
	@DeleteMapping("/{ean}")
	public ResponseEntity<Void> deleta(@PathVariable("ean") final Long ean) {
		final var deletou = this.service.deleta(ean);
		if(deletou) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.build();
		}
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

	@Operation(
			summary = "Serviço para buscar um produto"
	)
	@GetMapping("/{ean}")
	public ResponseEntity<ProdutoDTO> busca(@PathVariable("ean") final Long ean) {
		final var produto = this.service.busca(ean);
		if(Objects.nonNull(produto)) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(produto);
		}
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

}
