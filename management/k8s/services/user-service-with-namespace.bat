@echo off

kubectl create namespace msa-namespace
kubectl apply -f user-service-deployment.yaml
kubectl apply -f user-service-service.yaml

echo All resources have been applied successfully.
pause
