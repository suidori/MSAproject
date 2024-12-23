@echo off

kubectl apply -f mariadb-secret.yaml
kubectl apply -f mariadb-pvc.yaml
kubectl apply -f mariadb-deployment.yaml
kubectl apply -f mariadb-service.yaml

echo All resources have been applied successfully.
pause
