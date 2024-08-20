# language: pt

Funcionalidade: Teste de atualização de item

  Cenário: Atualiza item com sucesso
    Dado que tenho os dados validos de um item que ja esta cadastrado
    Quando atualizo esse item
    Entao recebo uma resposta que o item foi atualizado com sucesso

  Cenário: Atualiza item não cadastrado
    Dado que tenho os dados validos de um item
    Quando atualizo esse item
    Entao recebo uma resposta que o item nao esta cadastrado
