@echo off

kubectl create namespace msa-namespace
kubectl apply -f mariadb-secret.yaml
kubectl apply -f private-key.yaml
kubectl apply -f public-key.yaml

echo All resources have been applied successfully.
pause
