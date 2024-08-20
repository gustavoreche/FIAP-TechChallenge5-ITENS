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
import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM_COM_EAN;
import static io.restassured.RestAssured.given;


public class DeletaItemSteps {

    private Response response;
    private Long ean;
    private final String token = JwtUtil.geraJwt();

    @Dado("que informo um item que ja esta cadastrado")
    public void queInformoUmItemQueJaEstaCadastrado() {
        this.ean = System.currentTimeMillis();
        final var request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post(URL_ITEM);
    }

    @Dado("que informo um item nao cadastrado")
    public void queInformoUmItemNaoCadastrado() {
        this.ean = System.currentTimeMillis();
    }


    @Quando("deleto esse item")
    public void deletoEsseItem() {
        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(URL_ITEM_COM_EAN.replace("{ean}", this.ean.toString()));
    }

    @Entao("recebo uma resposta que o item foi deletado com sucesso")
    public void receboUmaRespostaQueOItemFoiDeletadoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;
    }

    @Entao("recebo uma resposta que o item nao foi cadastrado")
    public void receboUmaRespostaQueOItemNaoFoiCadastrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;
    }

}
