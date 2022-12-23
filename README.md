# kubernetes_v3

## Componentes principais do k8s
- geralmente ficam nos nós mestres
- api-server
 - lida com instruções enviadas ao k8s (recebidas via api restfull)
- scheduler
 - decide as cargas de trabalho em cada nó
- controller manager
 - garante que a configuração do cluster desejada, seja implementada, executando loops de controle
- etcd
 - é uma armazenamento chave valor, que contém a configuração do cluster (existe uma réplica em cada nó mestre)

### Nós
- aonde a carga de trabalho é executada (aplicações)
- possui alguns componentes que permite a comunicação com o control-plane
- kubelet -> garante que os contêineres estejam sendo executados, conforme a configuração do cluster
- kube-proxy -> fornece uma camada proxy de rede para as cargas de trabalho em cada nós (manifestos como service e ingress)
- container-runtime -> utilizado para executar o container (docker pos ex)

### Addons
- são componentes adicionais que fornecem funcionalidade ao cluster

### Namespace
- isolamento de recursos dentro do cluster

### Modos de autorização

#### RBAC
- controle de acesso baseado em função
- dentro do k8s existem 4 tipos:
 - role -> restrito ao uso dentro de um namepsace
 - cluster role -> uso que afeta o cluster por inteiro
 - rolebinding/clusterbinding -> associa a role a um usuário ou lista de usuário


#### ABAC
- acesso baseado em atributos
- faz uso de políticas em vez de funções
- as políticas tem as diretivas para qual recurso e qual tipo de usuário poderá acessa-lo.

#### Contextos
- combinação entre cluster, usuario e namespace

## Pods
- menor unidade dentro do k8s
- aonde são executados os containers
- ideal que dentro de cada pod seja executa apenas um container, no entanto há situações aonde fazemos o uso de sidecars, para coleta de metricas