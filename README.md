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

### Selector
- utilizado para identificar os pods que estão sendo executados

### Template
- aonde colocamos as especificações do nosso pod.

## Deployments
- possui vantagem sobre o replicaset, na verdade o deployment controla o replicaset
- podemos controlar a forma de atualização das nossa aplicação

## Horizontal pod autoscaler
- modifica o deployment, replicaset ou replicationController, mudando o número de pos
- conforme as metricas (memória ou cpu) salientadas no seu manifesto, de forma automática.

### Estrategias de deployments
- recreate
  - substitui todos os pods pela nova versão
  - não nos da margem para rollback
- rollingupdate
  - as implantações são mais lentas
  - mais controladas
  - um novo aplicativo será lançado aos poucos, pod por pod
  - podemos informar valores nos campos maxSurge e maxUnavailable, para ajustar
  - lembrando que a atualização seguirá, na substituição do pod, caso os atualizados subirem com sucesso
    -maxUnavailable: permite que você ajuste o número maximo de pods indisponíveis durante a atualização
    -maxSurge: permite ajustar o número máximo de pods que podemos atualizar imediatamente

## DaemonSet
- uma replica do pod e fixado 1 por nó
- ideal para aplicações que possuem a finalidade de coleta de métricas

## StatefulSets
- ele mantem a identidade de cada nó e sua ordem
- ideal para aplicações que possuem estado (statefull)
- como banco de dados

## Jobs
- executar tarefas que podem ser concluidas, que não são ideais para app de long duração
- ao finalizar a tarefa, o job acaba

## CronJobs
- recurso no kubernetes para execução de trabalho agendado

# Services e ingress
- para acessar um aplicação dentro do cluster ou para aplicações se comunicarem dentro do cluster, precismos utilizar o service
- existem 3 tipos de service:
  -  cluster ip
  - nodeport
  - external name
  - load balance
- uma outra forma de acessar uma app dentro do clusters, é através de roteamente dns, aonde entra o ingress


## Services Dns dentro do kubernetes
- para acessar um service dentro do seu namespace, passamos apenas o nome do service
- para acessar um service em outro namespace, precisamos passar o service.namespace.svc.cluster.local

### Proxy
- existem alguns algoritmos que podemos configurar no service para efetuar um redirecionamento das requisições aos pods vinculados (pelo nome do pod no selector)
- por padrão é o algoritmo round robin
 - simplesmente o pod usado na requisição vai para o final da fila
 - ele será novamente acionado, quando chegar a ser o primeiro da fila

### ClusterIp
- tipo se serviço que será acessado internamente no cluster apenas

### NodePort
- gera uma porta para acesso externo ao nosso service

### ingress
- faz o roteamento de chamadas para os serviços, através de um dns
- somente o ingress não funcionará, precisamos de um ingress-controller, o mais famoso é o nginx controller
```
para habilitar no minikube:
minikube addons enable ingress
```

# Configmap
- aonde podemos colocar as configurações das nossas aplicações, que estão rodando nos pods
- podemos injetar as configurações inseridas no configmap as apps, via arquivo ou variável de ambiente
  - arquivo utiliza volumes 

# Secrets
- armazenamento de informações sensíveis, que podem ser utilizadas pelas nossas apps.
- texto armazenado é em base64

# Atualização
```
A partir da versão 1.27 do Kubernetes, você pode aproveitar as vantagens do recurso chamado “In-Place Vertical Pod Scaling” . Ele permite que os usuários redimensionem os recursos de CPU/memória alocados para pods sem reiniciar os contêineres. Essa abordagem pode nos ajudar a acelerar a inicialização do Java no Kubernetes e, ao mesmo tempo, manter limites de recursos adequados (especialmente limites de CPU) para o aplicativo. Você pode ler mais sobre isso no seguinte artigo: https://piotrminkowski.com/2023/08/22/resize-cpu-limit-to-speed-up-java-startup-on-kubernetes/ .
```
- o recurso encontra-se em alpha, para habilitar no minikube:
```
minikube start --memory='8g' \
  --feature-gates=InPlacePodVerticalScaling=true
```
