package com.fiap.techchallenge5.bdd;

import com.fiap.techchallenge5.infrastructure.item.controller.dto.CriaItemDTO;
import com.fiap.techchallenge5.integrados.JwtUtil;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM;
import static com.fiap.techchallenge5.infrastructure.item.controller.ItemController.URL_ITEM_COM_EAN;
import static io.restassured.RestAssured.given;


public class BuscaItemSteps {

    private Response response;
    private Long ean;
    private String token;
    private ClientAndServer mockServerUsuario;

    @Dado("que busco um item que ja esta cadastrado")
    public void queBuscoUmItemQueJaEstaCadastrado() {
        this.token = JwtUtil.geraJwt();
        this.ean = System.currentTimeMillis();
        final var request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.mockServerUsuario = this.criaMockServerUsuario();

        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post(URL_ITEM);
    }

    @Dado("que busco um item nao cadastrado")
    public void queBuscoUmItemNaoCadastrado() {
        this.token = JwtUtil.geraJwt();
        this.ean = System.currentTimeMillis();

        this.mockServerUsuario = this.criaMockServerUsuario();
    }

    @Dado("que busco um item com um usuário que não existe no sistema")
    public void queBuscoUmItemComUmUsuarioQueNaoExisteNoSistema() {
        this.token = JwtUtil.geraJwt("USER", "novoLogin");
        this.ean = System.currentTimeMillis();

        this.mockServerUsuario = this.criaMockServerUsuario();
    }

    @Quando("busco esse item")
    public void buscoEsseItem() {
        RestAssured.baseURI = "http://localhost:8081";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL_ITEM_COM_EAN.replace("{ean}", this.ean.toString()));
    }

    @Entao("recebo uma resposta que o item foi encontrado com sucesso")
    public void receboUmaRespostaQueOItemFoiEncontradoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;

        this.mockServerUsuario.stop();
    }

    @Entao("recebo uma resposta que o item nao foi encontrado")
    public void receboUmaRespostaQueOItemNaoFoiEncontrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;

        this.mockServerUsuario.stop();
    }

    private ClientAndServer criaMockServerUsuario() {
        final var clientAndServer = ClientAndServer.startClientAndServer(8080);

        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/usuario/teste")
                                .withHeader("Authorization", "Bearer " + this.token)
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody(String.valueOf(true))
                );

        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/usuario/novoLogin")
                                .withHeader("Authorization", "Bearer " + this.token)
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                                .withStatusCode(204)
                );

        return clientAndServer;
    }

}
