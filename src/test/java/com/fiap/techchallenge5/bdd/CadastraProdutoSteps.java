package com.fiap.techchallenge5.bdd;

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
import static io.restassured.RestAssured.given;


public class CadastraProdutoSteps {

    private Response response;
    private CriaProdutoDTO request;
    private final Long ean = System.currentTimeMillis();

    @Dado("que tenho dados validos de um produto")
    public void tenhoDadosValidosDeUmProduto() {
        this.request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                new BigDecimal("10.00"),
                10L
        );
    }

    @Dado("que tenho dados validos de um produto que ja esta cadastrado")
    public void tenhoDadosValidosDeUmProdutoQueJaEstaCadastrado() {
        this.request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.cadastroEsseProduto();
    }

    @Quando("cadastro esse produto")
    public void cadastroEsseProduto() {
        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.request)
                .when()
                .post(URL_PRODUTO);
    }

    @Entao("recebo uma resposta que o produto foi cadastrado com sucesso")
    public void receboUmaRespostaQueOProdutoFoiCadastradoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;
    }

    @Entao("recebo uma resposta que o produto ja esta cadastrado")
    public void receboUmaRespostaQueOProdutoJaEstaCadastrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
        ;
    }

}
