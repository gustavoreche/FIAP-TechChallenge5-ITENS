package com.fiap.techchallenge5.infrastructure.token.client;

import com.fiap.techchallenge5.infrastructure.token.client.response.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "token", url = "http://172.17.0.1:8080/auth/login")
public interface TokenClient {

    @PostMapping(value = "/{cpf}")
    TokenDTO geraToken(@PathVariable(value = "cpf") final String cpf);

}
