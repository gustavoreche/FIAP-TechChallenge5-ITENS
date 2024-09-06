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
import static io.restassured.RestAssured.given;


public class CadastraItemSteps {

    private Response response;
    private CriaItemDTO request;
    private final Long ean = System.currentTimeMillis();
    private String token;
    private ClientAndServer mockServerUsuario;

    @Dado("que tenho dados validos de um item")
    public void tenhoDadosValidosDeUmItem() {
        this.token = JwtUtil.geraJwt();
        this.request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.mockServerUsuario = this.criaMockServerUsuario();
    }

    @Dado("que tenho dados validos de um item que ja esta cadastrado")
    public void tenhoDadosValidosDeUmItemQueJaEstaCadastrado() {
        this.token = JwtUtil.geraJwt();
        this.request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.mockServerUsuario = this.criaMockServerUsuario();

        this.cadastroEsseItem();
    }

    @Dado("que cadastro um item com um usuário que não existe no sistema")
    public void queCadastroUmItemComUmUsuarioQueNaoExisteNoSistema() {
        this.token = JwtUtil.geraJwt("ADMIN", "novoLogin");
        this.request = new CriaItemDTO(
                this.ean,
                "Item Teste",
                new BigDecimal("10.00"),
                10L
        );

        this.mockServerUsuario = this.criaMockServerUsuario();
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

        this.mockServerUsuario.stop();
    }

    @Entao("recebo uma resposta que o item não foi cadastrado")
    public void receboUmaRespostaQueOItemNaoFoiCadastrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
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
