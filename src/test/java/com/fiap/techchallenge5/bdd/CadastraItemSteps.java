package com.fiap.techchallenge5.bdd;

import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.integrados.JwtUtil;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM;
import static io.restassured.RestAssured.given;


public class CadastraItemSteps {

    private Response response;
    private CriaItemDTO request;
    private final Long ean = System.currentTimeMillis();
    private final String token = JwtUtil.geraJwt();

    @Dado("que tenho dados validos de um item")
    public void tenhoDadosValidosDeUmItem() {
        this.request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );
    }

    @Dado("que tenho dados validos de um item que ja esta cadastrado")
    public void tenhoDadosValidosDeUmItemQueJaEstaCadastrado() {
        this.request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.cadastroEsseItem();
    }

    @Quando("cadastro esse item")
    public void cadastroEsseItem() {
        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(this.request)
                .when()
                .post(URL_ITEM);
    }

    @Entao("recebo uma resposta que o item foi cadastrado com sucesso")
    public void receboUmaRespostaQueOItemFoiCadastradoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;
    }

    @Entao("recebo uma resposta que o item ja esta cadastrado")
    public void receboUmaRespostaQueOItemJaEstaCadastrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
        ;
    }

}
