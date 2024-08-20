package com.fiap.techchallenge5.performance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PerformanceTestSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8081");

    ActionBuilder cadastraProdutoRequest = http("cadastra produto")
            .post("/produto")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                              {
                                "ean": "${ean}",
                                "nome": "Produto Teste",
                                "preco": "15.00",
                                "quantidade": 100
                              }
                    """))
            .check(status().is(201));

    ActionBuilder atualizaProdutoRequest = http("atualiza produto")
            .put("/produto/${ean}")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                              {
                                "nome": "Produto Teste 20",
                                "preco": "15.00",
                                "quantidade": 100
                              }
                    """))
            .check(status().is(200));

    ActionBuilder deletaProdutoRequest = http("deleta produto")
            .delete("/produto/${ean}")
            .header("Content-Type", "application/json")
            .check(status().is(200));

    ActionBuilder buscaProdutoRequest = http("busca produto")
            .get("/produto/${ean}")
            .header("Content-Type", "application/json")
            .check(status().is(200));

    ScenarioBuilder cenarioCadastraProduto = scenario("Cadastra produto")
            .exec(session -> {
                long ean = System.currentTimeMillis();
                return session.set("ean", ean);
            })
            .exec(cadastraProdutoRequest);

    ScenarioBuilder cenarioAtualizaProduto = scenario("Atualiza produto")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 123456789L;
                return session.set("ean", ean);
            })
            .exec(cadastraProdutoRequest)
            .exec(atualizaProdutoRequest);

    ScenarioBuilder cenarioDeletaProduto = scenario("Deleta produto")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 333333333L;
                return session.set("ean", ean);
            })
            .exec(cadastraProdutoRequest)
            .exec(deletaProdutoRequest);

    ScenarioBuilder cenarioBuscaProduto = scenario("Busca produto")
            .exec(session -> {
                long ean = System.currentTimeMillis() + 777777777L;
                return session.set("ean", ean);
            })
            .exec(cadastraProdutoRequest)
            .exec(buscaProdutoRequest);


    {
        setUp(
                cenarioCadastraProduto.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioAtualizaProduto.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioDeletaProduto.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioBuscaProduto.injectOpen(
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
}