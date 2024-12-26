@echo off

kubectl create namespace mariadb-namespace
kubectl apply -f .

echo All resources have been applied successfully.
pause
