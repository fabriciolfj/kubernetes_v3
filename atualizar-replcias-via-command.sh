kubectl scale deployment myapp --replicas=5

##dar rollback
kubectl rollout undo deployment myapp

##dar rollback para uma versao especifica
kubectl rollout undo deployment myapp -to-revision=2