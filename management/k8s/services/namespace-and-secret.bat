@echo off

kubectl create namespace msa-namespace
kubectl apply -f mariadb-secret.yaml

echo All resources have been applied successfully.
pause
