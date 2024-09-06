# language: pt

Funcionalidade: Teste de deletar o item

  Cenário: Deleta item com sucesso
    Dado que informo um item que ja esta cadastrado
    Quando deleto esse item
    Entao recebo uma resposta que o item foi deletado com sucesso

  Cenário: Deleta item não cadastrado
    Dado que informo um item nao cadastrado
    Quando deleto esse item
    Entao recebo uma resposta que o item nao foi deletado

  Cenário: Deleta item com usuário que não existe no sistema
    Dado que deleto um item com um usuário que não existe no sistema
    Quando deleto esse item
    Entao recebo uma resposta que o item nao foi deletado
