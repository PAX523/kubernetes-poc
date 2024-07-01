# Kubernetes PoC

Consists of: external service, internal service, database

![Architecture](Specification.png)

## Run on Kubernetes

### Install the Nginx Ingress Controller:

```shell
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install nginx-ingress ingress-nginx/ingress-nginx
```

### Install Cert-Manager:

```shell
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.14.5/cert-manager.yaml
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager --namespace cert-manager --create-namespace
```

### Preparations:

- Build the images for `internal-service` and `external-service` by running the `deploy.sh` scripts and terminate them again
- Run minikube:

```shell
minikube start --apiserver-ips 0.0.0.0
minikube dashboard
```

- Add the built images to the local minikube registry:

```shell
minikube image load image-name:tag
```

- Inspect the minikube VM:

```shell
minikube ssh
```

### Deploy resources:

```shell
mkdir -p /tmp/database/mysql/

kubectl apply -f config.yaml
kubectl apply -k database-pv/local/
kubectl apply -f database-pvc.yaml

kubectl apply -f database-svc.yaml
kubectl apply -f internal-svc.yaml
kubectl apply -f external-svc.yaml

kubectl apply -f database-sts.yaml
kubectl apply -f internal-deploy.yaml
kubectl apply -f external-deploy.yaml

kubectl apply -f database-netpol.yaml
kubectl apply -f internal-netpol.yaml
kubectl apply -f external-netpol.yaml
```

### Update deployment:

Apply the YAML files again.

### Get IP for minikube:

Used internal IP:

```shell
kubectl get nodes -o wide
```

### Ping External Service:

```shell
minikube ssh -- curl -i http://localhost:30000/ping/pong
```

### Remove deployment:

```shell
kubectl delete netpol --all -n default
kubectl delete deploy --all -n default
kubectl delete sts --all -n default

kubectl delete svc --all -n default

kubectl delete pvc --all -n default
kubectl delete pv --all -n default

kubectl delete cm --all -n default
kubectl delete secret --all -n default

minikube ssh -- sudo rm -rf /tmp/database/mysql/* # optional: delete database
```

### Script snippets to test endpoints:

```shell
# ping database container via nc
nc -vz database-sts-0.mysql-server-h.default.svc.cluster.local 3306

# ping internal service container
/usr/bin/curl http://internal-svc:8081/ping/pong

# ping database container via mysql
mysql -h database-sts-0.mysql-server-h.default.svc.cluster.local -u root -pa
```

### DigitalOcean:

Expected image names: `registry.digitalocean.com/<your-registry-name>/<image-name>:<tag>`

Connect with DigitalOcean: `doctl registry login`
