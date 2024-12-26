@echo off

kubectl apply -f auth-service-deployment.yaml
kubectl apply -f auth-service-service.yaml

echo All resources have been applied successfully.
pause
