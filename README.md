# altBank
apenas informativo, nos endpoints onde se utilizan a busca por filtro, é passado um objeto com as propriedades a serem filtradas, 
por exemplo , para que eu busque todas a contas de um determinado cliente pelo documento, utilizei o CPF , mas poderia ser CNPJ etc.

http://{{SERVER}}:8080/administracao/conta/findByFilters/1/10?order=desc ou asc

{
    "pessoa": {
        "fisica": {
            "numCpf": 27769157803
        }
    }
}

a propriedade order na composição da request ordena os registros retornados pelo ID , asc ou desc.

1 ) foi pedido para que fosse possivel desabilitar/habilitar uma conta, implementado da seguinta forma nesta rota :

http://{{SERVER}}:8080/administracao/conta/disableAccount/2 

por exemplo :

{
    "id": 5,
    "agencia": 330,
    "numero": 15645,
    "saldo": 177.45,
    "limiteCredito": 1850.00,
    "inativada": false,
    "pessoa": {
        "id": 3,
        "fisica": {
            "id": 4
        }
    }
}

ou

{
    "id": 5,
    "agencia": 330,
    "numero": 15645,
    "saldo": 177.45,
    "limiteCredito": 1850.00,
    "inativada": true,
    "pessoa": {
        "id": 3,
        "fisica": {
            "id": 4
        }
    }
}

caso seja feito uma request para consultar esta conta e ela esteja inativada,  retorna a um objeto com a seguinte mensagem :

"Conta Desabilitada";

caso contrário detorna o objeto conta normalmente.

2) um determinado cartao quando emitido, seu status inicial é inativo e bloqueado, por ex.
pode ser alterado na segunda rota o seu estado, para ativo e vice-versa.

http://{{SERVER}}:8080/administracao/cartao/update/6

{
    "id": 6,
    "numeroCartao": "458545865478584",
    "bandeira": "VISA",
    "limiteCredito": 1580.00,
    "nomeTitular": "MARCELO PAULO REBELLO MARTINS",
    "codigoSeguranca": 357.00,
    "ativo": true,
    "bloqueado": true,
    "entregueValidado": false,
    "status": "BLOQUEADO",
    "pessoa": {
        "id": 3
    },
    "cartoesPessoa": []
}

ou 

{
    "id": 6,
    "numeroCartao": "458545865478584",
    "bandeira": "VISA",
    "limiteCredito": 1580.00,
    "nomeTitular": "MARCELO PAULO REBELLO MARTINS",
    "codigoSeguranca": 357.00,
    "ativo": true,
    "bloqueado": false,
    "entregueValidado": true,
    "status": "ENTREGUE",
    "pessoa": {
        "id": 3
    },
    "cartoesPessoa": []
}

3 ) é possivel solicitar um cartao virtual desde que o status do cartao esteja como entregue, supondo que o cartão tenha sido entregue, internamente no 
sistema esta informação também já esta alterada como entregue, na rota http://{{SERVER}}:8080/administracao/cartao/requestVirtualCard/6
podemos consultar este status e é retornado a seguinte mensagem caso a propriedade entregueValidado do objeto cartao esteja false.

retorna um badRequest mais a mensagem  : ("CARTÃO NÃO ENTREGUE/VALIDADO")));
   caso true , retorna 400 com a mensagem  ("CARTÃO VIRUTAL SOLICITADO")));

o case de uso acima é apenas para exemplificar esta funcionalidade, obviamente outras tratitivas seriam necessárias.
