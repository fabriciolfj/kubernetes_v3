#mvn spring-boot:build-image
mvn -Pnative spring-boot:build-image
docker push fabricio211/person-service:1.0.3


minikube image load fabricio211/person-service:1.0.3 --profile minikube