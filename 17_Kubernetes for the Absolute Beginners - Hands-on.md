### Terminology

- Node (Worker Node)
    - Physical/virtual machine
    - Called "Minions" in the past
    - Also runs the Kubelet

- Cluster
    - Set of nodes

- Master (Master Node)
    - Special node that manages the worker nodes
    - Also runs the API Server, Controller, Scheduler
    - Stores management data into the etcd

- POD
    - Smallest object that can be created in Kubernetes
    - Executed by a node and runs 1 or more containers
        - Preferred is only 1 container per POD, except there are multiple containers that run different applications, e.g. some helper containers
        - Should aggregate all containers that belong together and have a 1-to-1 relationship and must be started/terminated altogether
    - Containers can communicate via localhost when they are in the same POD

Hierarchy:

- Cluster
    - Nodes
        - Deployment
            - Replica Set
                - PODs
                    - Containers

### Components of Kubernetes

- API Server
    - Front-end
    - User management
- etcd
    - Key-value store
    - Stores data to manage the cluster
    - Stores the data distributed
- Scheduler
    - Responsible to distributing work across multiple nodes
    - Assigns newly created containers and assigns them to nodes
- Controller
    - Notices when nodes or containers go down
    - Makes decision when to bring up new containers
- Container runtime
    - Software that runs containers (e.g. Docker, rkt, cri-o)
- Kubelet
    - Agent on each worker node
    - Makes sure that the containers are running on the nodes as expected

### Replicas

- Replication Controller (deprecated)
    - Older technology which was replaced by Replica Set
    - Able to replicate redundant PODs on single nodes or across all nodes in the same cluster

```yaml
apiVersion: v1
kind: ReplicationController
metadata:
  name: myapp-rc
  labels:
    app: myapp
    type: front-end
spec:
  replicas: 3
  template: # contains everything of a POD yaml except apiVersion and kind
    metadata:
      name: myapp-pod
      labels:
        app: myapp
        type: front-end
    spec:
      containers:
        - name: nginx-container
          image: nginx
```

- Replica Set
    - Same purpose, but more flexible
    - Process that monitors the PODs
    - PODs created by a Replica Set will get the name of the Replica Set from its metadata section
    - If you create more PODs with the same label, manually, that exceed the maximum managed number of replicas, it will be terminated

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: myapp-replicaset
  ...
spec:
  replicas: 3
  selector: # mandatory property which is not mandatory in Replication Controllers and determines which PODs apply to this Replica Set
    matchLabels:
      type: front-end
  template:
    ...
```

### Deployments

- Groups Replica Sets or PODs that belong together for one release and must later be updated and restarted altogether

```yaml
apiVersion: apps/v1
kind: Deployment
  ...same as ReplicaSet...
```

- Strategies when rolling out new deployment versions:
    - Recreate: terminates everything first, and starts new versions (downtime)
    - Rolling update (default): terminates and recreates one POD by one (no downtime)
- Perform rollout by simply:
    - Updating and applying the Kubernetes deployment file: `kubectl apply -f ...`
    - Non-persistent in memory: `kubectl set image deployment/myapp-deployment the-container-name=the-image-name:1.2.3`

### Networking

- Each Node has its IP address
- Each POD has its own IP addresses
- The underlying networking solution makes sure that all PODs have distinct IP addresses even across different deployments

### Services

- For communication between components (are between them)

```
POD <---> Service <---> Database
```

- Types:
    - NodePort: Enables communication via a provided external port to the internal POD's port
        - External port (`nodePort`) forwards to internal service port (`port`)
        - Internal service port (`port`) forwards to internal POD port (`targetPort`)
        - Within the cluster, it has its internal IP (Cluster IP)
        - Valid `nodePort` range: 30000 - 32767
        - Typically used for external access by users
    - ClusterIP: Creates an internal virtual IP for communication
        - Can be accessed by other PODs by the cluster IP or the service name
        - Typically used for internal access between services
    - LoadBalancer: Proxy which allows to communicate with distributed nodes
        - Use case as for NodePort
        - Is preferred in production environments

```yaml
apiVersion: v1
kind: Service
metadata:
  ...
spec:
  type: NodePort # or the others
  ports:
    - targetPort: 80 # if omitted, the same as port
      port: 80
      nodePort: 30008 # if omitted, random port
  selector:
    - app: myapp
      tier: front-end
```

```yaml
apiVersion: v1
kind: Service
...
spec:
  type: ClusterIP # default type
  ports:
    - targetPort: 80
      port: 80
  selector:
    - app: myapp
      tier: back-end
```

To get available URL for accessing on minikube: `minikube service myapp-serivce --url`

### Design approach:

![Example application](example-app.png)

- Specify POD files for each service
    - typical labels: `name` (can match name of the POD), `app` (title of the application)
- Create Service files for each service that gets accessed internally or externally
    - For internal service access chose type `ClusterIP` (default) and specify `targetPort` and `port` with the same value as the exposed port of
      the service
    - For external service access chose type `NodePort` and specify `targetPort` and `port` with the same value as the exposed port of the service
      and add `nodePort` with the desired port to be exposed for external access
    - The name of the Service should match the expected hostname for the connection (e.g. `mysql` for `jdbc:mysql://mysql:3306`)
    - In production (and if supported), you can replace the Service type `NodePort` of your external services to `LoadBalancer`
- Create one or more deployment files
    - Aggregate what belongs together and always must be rolled out altogether
    - Consider that this doesn't mean that you group a service and a database application into one single POD or respectively one single deployment
      (always think about replicas: is it reasonable to have more than one replica for that POD with that containers in it?)
    - Services are not wrapped into a Deployment

### Local setup

- Install `kubectl` as described on http://kubernetes.io/

### Management of applications

Via command line tool `kubectl`.

Deploy application:

```shell
kubectl run hello-minikube

  or

kubectl run arbitrary-deployment-name --image=hello-minikube
```

Show cluster information:

```shell
kubectl cluster-info
```

List all nodes of a cluster:

```shell
kubectl get nodes
```

List all PODs of a node:

```shell
kubectl get pods

kubectl get pods -o wide
```

More information of one POD:

```shell
kubectl describe pod deployment-name 
```

Remove a POD:

```shell
kubectl delete pod deployment-name
```

List all Replication Controllers:

```shell
kubectl get replicationcontrollers
```

List all Replica Sets:

```shell
kubectl get replicaset

  or

kubectl get rs
```

List all Deployments:

```shell
kubectl get deployments
```

View deployment rollout status of ongoing rollout:

```shell
kubectl rollout status deployment/my-deployment
```

View deployment revisions:

```shell
kubectl rollout history deployment/my-deployment
```

Revert latest deployment rollout to previous version:

```shell
kubectl rollout undo deployment/myapp-deployment
```

Edit the Yaml of a running Replicate Set (or something else) on runtime in memory (change is not persistent):

```shell
kubectl edit replicaset the-rep-set-deployment-name

  or for specific property:

kubectl scale replicaset the-rep-set-deployment-name --replicas=5
```

Create something w/o yaml file, e.g. Deployment:

```shell
kubectl create deployment the-name --image=the-base-image --replicas=3
```

View services:

```shell
kubectl get services
```

List desired objects at the same time:

```shell
kubectl get pods,services
```

List all objects of the cluster:

```shell
kubectl get all
```

### Kubernetes files

POD:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels: # can contain any arbitrary key-values
    app: myapp
    type: front-end

spec:
  containers:
    - name: nginx-container
      image: nginx
      ports:
        - containerPort: 80 # port to be exposed to the internal container network
      env: # for environment variables to be set
        - name: DATABASE_USER
          value: "ADMIN"
```

Deployment:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: front-end-deployment
  labels:
    app: myapp
    type: myapp-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      type: front-end
  template:
    metadata:
      name: myapp-pod
      labels:
        app: myapp
        type: front-end
    spec:
      containers:
        - name: nginx-container
          image: nginx
```

Service:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: front-end-service
  labels:
    app: myapp
    type: myapp-service
spec:
  type: NodePort
  ports:
    - targetPort: 80
      port: 80
      nodePort: 30008
  selector:
    - app: myapp
      tier: front-end
```

```shell
kubectl create -f pod-definition.yml

  or

kubectl apply -f pod-definition.yml
```

`apply` updates changed properties in running deployments, but doesn't remove deleted properties.

`replace` recreates existing deployments and also removes deleted properties as specified by the yaml.

Apply all Kubernetes files in the folder:

```shell
kubectl apply -f .
```
