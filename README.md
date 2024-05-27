# Kubernetes PoC

Consists of: external service, internal service, database

## Run on Kubernetes

### Install the Nginx Ingress Controller:

```shell
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install nginx-ingress ingress-nginx/ingress-nginx
```

### Install Cert-Manager:

```shell
kubectl apply --validate=false -f https://github.com/cert-manager/cert-manager/releases/download/v1.7.2/cert-manager.crds.yaml
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager --namespace cert-manager --create-namespace
```

### Deploy resources:

```shell
kubectl apply -f 01-mysql-deployment.yaml
kubectl apply -f 02-internal-service-deployment.yaml
kubectl apply -f 03-external-service-deployment.yaml
kubectl apply -f 04-external-service-ingress.yaml
kubectl apply -f 05-letsencrypt-clusterissuer.yaml
```

### Update deployment:

Apply the YAML files again.

### Remove deployment:

```shell
kubectl delete deployment external-service
kubectl delete service external-service
kubectl delete ingress external-service-ingress
```
