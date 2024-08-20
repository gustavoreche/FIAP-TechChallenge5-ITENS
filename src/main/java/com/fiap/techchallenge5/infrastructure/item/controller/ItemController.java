package com.fiap.techchallenge5.infrastructure.item.controller;

import com.fiap.techchallenge5.infrastructure.item.controller.dto.AtualizaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.infrastructure.item.controller.dto.ItemDTO;
import com.fiap.techchallenge5.useCase.item.ItemUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(
		name = "Itens",
		description = "Serviço para realizar o gerenciamento de itens no sistema"
)
@RestController
@RequestMapping(ItemController.URL_ITEM)
public class ItemController {

	public static final String URL_ITEM = "/item";
	public static final String URL_ITEM_COM_EAN = URL_ITEM + "/{ean}";

	private final ItemUseCase service;

	public ItemController(final ItemUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para cadastrar um item"
	)
	@PostMapping
	public ResponseEntity<Void> cadastra(@RequestBody @Valid final CriaItemDTO dadosItem) {
		final var cadastrou = this.service.cadastra(dadosItem);
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
			summary = "Serviço para atualizar um item"
	)
	@PutMapping("/{ean}")
	public ResponseEntity<Void> atualiza(@PathVariable("ean") final Long ean,
										 @RequestBody @Valid final AtualizaItemDTO dadosItem) {
		final var atualizou = this.service.atualiza(ean, dadosItem);
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
			summary = "Serviço para deletar um item"
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
			summary = "Serviço para buscar um item"
	)
	@GetMapping("/{ean}")
	public ResponseEntity<ItemDTO> busca(@PathVariable("ean") final Long ean) {
		final var item = this.service.busca(ean);
		if(Objects.nonNull(item)) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(item);
		}
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

}
