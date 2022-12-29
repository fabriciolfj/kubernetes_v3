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

### Teste do kubernetes
- readiness
  - verifica se um contêiner está pronto para executar uma função, como aceitar um trafego http
- liveness
  - para determinar se um aplicativo falhou por algum motivo
  - ideal para aplicações que ficam muito tempo sendo executadas
  - dentro do livenesse podemos definir o numero de failureThreshold (rediness tambem), caso ocorra falha no pod (na validação do liveness) na quantidade especificada, o kubernetes agirá
  - caso as falhas ultrapasse o número especificado no failureThreshold, o pod será marcada como não pronto
- startup
  - é executado apenas uma vez, na inicialização do contêiner
  - ideal para evitar que outras sondas sejam executadas, caso aja alguma falha
  - e também ideal para evitar reinicialização desnecessária (diante validação de outras sondas), em aplicações com inicialização lenta
  - existe o successThreshold, que podemos definir uma quantidade para dizer que , caso atendida, o pod será marcado como pronto para uso

### Status do pods
- pending
- running
- succeeded
- unknown
- failed
- obs: esses status são influenciados pelo restartPolicy definido no manifesto do pod, que podem ser:
  - never
  - always
  - onfailure

### Controladores de pods
- os controladores verificam o estado desejado do cluster, comparando com o atual
- caso aja divergência, este toma medidas de correção.
- os controladores de pods mais utilizados são:
  - replicaset
  - deployment
  - daemonset

## ReplicaSet
- mais simples
- permiti dizer ao kubernetes quantos pods queremos de tal app
- caso algum pode quebre/falhe, outro será criado para contemplar a quantidade de instancias específicadas yaml
- lembrando que após a criação do container, as sondas que influenciam o status do pod