package com.fiap.techchallenge5.bdd;

import com.fiap.techchallenge5.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge5.infrastructure.controller.dto.CriaProdutoDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.fiap.techchallenge5.infrastructure.controller.ProdutoController.URL_PRODUTO;
import static com.fiap.techchallenge5.infrastructure.controller.ProdutoController.URL_PRODUTO_COM_EAN;
import static io.restassured.RestAssured.given;


public class AtualizaProdutoSteps {

    private Response response;
    private AtualizaProdutoDTO request;
    private Long ean;

    @Dado("que tenho os dados validos de um produto que ja esta cadastrado")
    public void tenhoOsDadosValidosDeUmProdutoQueJaEstaCadastrado() {
        this.ean = System.currentTimeMillis();
        final var request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                new BigDecimal("10.00"),
                10L
        );

        RestAssured.baseURI = "http://localhost:8081";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_PRODUTO);

        this.request = new AtualizaProdutoDTO(
                "Produto Teste",
                new BigDecimal("15.00"),
                10L
        );
    }

    @Dado("que tenho os dados validos de um produto")
    public void tenhoOsDadosValidosDeUmProduto() {
        this.ean = System.currentTimeMillis();
        this.request = new AtualizaProdutoDTO(
                "Produto Teste",
                new BigDecimal("10.00"),
                10L
        );
    }


    @Quando("atualizo esse produto")
    public void atualizoEsseProduto() {
        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.request)
                .when()
                .put(URL_PRODUTO_COM_EAN.replace("{ean}", this.ean.toString()));
    }

    @Entao("recebo uma resposta que o produto foi atualizado com sucesso")
    public void receboUmaRespostaQueOProdutoFoiAtualizadoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;
    }

    @Entao("recebo uma resposta que o produto nao esta cadastrado")
    public void receboUmaRespostaQueOProdutoNaoEstaCadastrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;
    }

}
