@echo off

kubectl apply -f qna-service-deployment.yaml
kubectl apply -f qna-service-service.yaml

echo All resources have been applied successfully.
pause
