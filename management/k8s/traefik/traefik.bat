@echo off

kubectl create namespace traefik
kubectl apply -f 00-account.yml
kubectl apply -f 00-role.yml
kubectl apply -f 01-role-binding.yml
kubectl apply -f 02-traefik.yml
kubectl apply -f 02-traefik-config.yml
kubectl apply -f 02-traefik-services.yml
kubectl apply -f 03-traefik-ingress.yml

@REM kubectl apply -f .

echo All resources have been applied successfully.
pause
