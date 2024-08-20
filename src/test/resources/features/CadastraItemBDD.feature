# language: pt

Funcionalidade: Teste de cadastro de item

  Cenário: Cadastra item com sucesso
    Dado que tenho dados validos de um item
    Quando cadastro esse item
    Entao recebo uma resposta que o item foi cadastrado com sucesso

  Cenário: Cadastra item já cadastrado
    Dado que tenho dados validos de um item que ja esta cadastrado
    Quando cadastro esse item
    Entao recebo uma resposta que o item ja esta cadastrado
