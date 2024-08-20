# language: pt

Funcionalidade: Teste de buscar o item

  Cenário: Busca item com sucesso
    Dado que busco um item que ja esta cadastrado
    Quando busco esse item
    Entao recebo uma resposta que o item foi encontrado com sucesso

  Cenário: Busca item não cadastrado
    Dado que busco um item nao cadastrado
    Quando busco esse item
    Entao recebo uma resposta que o item nao foi encontrado
