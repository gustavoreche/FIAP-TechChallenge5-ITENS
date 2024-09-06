package com.fiap.techchallenge5.performance;

import com.fiap.techchallenge5.integrados.JwtUtil;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PerformanceTestSimulation extends Simulation {

    private final String token = JwtUtil.geraJwt();
    private final String tokenTeste1 = "Bearer " + this.token;
    private final ClientAndServer mockServerUsuario = this.criaMockServerUsuario(this.tokenTeste1);

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8081");


    ActionBuilder cadastraItemRequest = http("cadastra item")
            .post("/item")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .body(StringBody("""
                              {
                                "ean": "${ean}",
                                "nome": "Item Teste",
                                "preco": "15.00",
                                "quantidade": 100
                              }
                    """))
            .check(status().is(201));

    ActionBuilder atualizaItemRequest = http("atualiza item")
            .put("/item/${ean}")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .body(StringBody("""
                              {
                                "nome": "Item Teste 20",
                                "preco": "15.00",
                                "quantidade": 100
                              }
                    """))
            .check(status().is(200));

    ActionBuilder deletaItemRequest = http("deleta item")
            .delete("/item/${ean}")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .check(status().is(200));

    ActionBuilder buscaItemRequest = http("busca item")
            .get("/item/${ean}")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .check(status().is(200));

    ScenarioBuilder cenarioCadastraItem = scenario("Cadastra item")
            .exec(session -> {
                long ean = System.currentTimeMillis();
                return session.set("ean", ean);
            })
            .exec(cadastraItemRequest);

    ScenarioBuilder cenarioAtualizaItem = scenario("Atualiza item")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 123456789L;
                return session.set("ean", ean);
            })
            .exec(cadastraItemRequest)
            .exec(atualizaItemRequest);

    ScenarioBuilder cenarioDeletaItem = scenario("Deleta item")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 333333333L;
                return session.set("ean", ean);
            })
            .exec(cadastraItemRequest)
            .exec(deletaItemRequest);

    ScenarioBuilder cenarioBuscaItem = scenario("Busca item")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 777777777L;
                return session.set("ean", ean);
            })
            .exec(cadastraItemRequest)
            .exec(buscaItemRequest);


    {
        setUp(
                cenarioCadastraItem.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioAtualizaItem.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioDeletaItem.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioBuscaItem.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10)))
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(600),
                        global().failedRequests().count().is(0L));
    }

    private ClientAndServer criaMockServerUsuario(final String token1) {
        final var clientAndServer = ClientAndServer.startClientAndServer(8080);

        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/usuario/teste")
                                .withHeader("Authorization", token1)
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody(String.valueOf(true))
                );

        return clientAndServer;
    }

}