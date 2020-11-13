# Spring Cloud & GitLab CI/CD & Docker & Kubernetes Automatic Publishing
Simplest course all in one node on virtualbox 

##	prerequisite
- centos7
>memory 6g more better  
core 4  
hard 100g
- install docker  
refer to https://juejin.im/post/6844903943051411469
- vim /etc/docker/daemon.json
>{
   "registry-mirrors": [
       "https://registry.docker-cn.com",
       "https://hub-mirror.c.163.com/",
       "https://xyjqe58w.mirror.aliyuncs.com"],
   "exec-opts": ["native.cgroupdriver=systemd"],
   "insecure-registries" : ["192.168.137.5:5000", "k8s-master:5000"],
   "log-driver": "json-file",
   "log-opts": {
     "max-size": "100m"
   },
   "storage-driver": "overlay2"
 }
- vim /usr/lib/systemd/system/docker.service  
>ExecStart=/usr/bin/dockerd -H unix:// -H tcp://0.0.0.0:2375 --containerd=/run/containerd/containerd.sock  
- systemctl daemon-reload  
- systemctl restart docker  
- setup local docker registry
sudo docker run -d \
 -p 5000:5000 \
 --restart=always \
 --name registry \
 -v /data/docker-registry:/var/lib/registry \
 registry:latest
 
## minikube install
- download & setup minikube  
curl -Lo minikube http://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/releases/v1.2.0/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
- launch minikube  
minikube start --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers  --registry-mirror=https://registry.docker-cn.com --driver=none
- start dashboard  
minikube dashboard 
- setup dashboard proxy  
kubectl proxy --address 0.0.0.0 --accept-hosts='^.*' 
- delete minikube  
minikube delete && rm -rf ~/.minikube && rm -rf ~/.kube

## install docker gitlab
- setup gitlab  
docker run -d \
   --hostname k8s-master \
   --name gitlab --restart always \
   -p 443:443 -p 80:80 -p 222:22 -p 5005:5005 \
   -v /root/gitlab/config:/etc/gitlab \
   -v /root/gitlab/logs:/var/log/gitlab \
   -v /root/gitlab/data:/var/opt/gitlab \
   beginor/gitlab-ce
- setup gitlab runner  
docker run -d --name gitlab-runner --restart always \
  -v /root/gitlab/runner/config:/etc/gitlab-runner \
  -v /var/run/docker.sock:/var/run/docker.sock \
registry.cn-hangzhou.aliyuncs.com/yuheng/gitlab-runner
- register gitlab's share runner  
docker run --rm -t -i -v  /root/gitlab/runner/config:/etc/gitlab-runner registry.cn-hangzhou.aliyuncs.com/yuheng/gitlab-runner register  

>Please enter the gitlab-ci coordinator URL (e.g. https://gitlab.com )  
http://k8s-master

>Please enter the gitlab-ci token for this runner  
go to http://k8s-master get token input here  
setting >> runners >> Setup a shared Runner manually  

>Please enter the gitlab-ci description for this runner  
runner1

>Please enter the gitlab-ci tags for this runner (comma separated):  
anything

>Please enter the executor: ssh, docker+machine, docker-ssh+machine, kubernetes, docker, parallels, virtualbox, docker-ssh, shell:  
docker

>Please enter the Docker image (eg. ruby:2.1):  
docker:18.09.9
- vim /root/gitlab/runner/config/config.toml 
>[[runners]]  
  name = "runner1"  
  url = "http://k8s-master/"  
  token = "a11337ce02ddf5b8d091390d583afa"  
  executor = "docker"  
  [runners.custom_build_dir]  
  [runners.cache]  
    [runners.cache.s3]  
    [runners.cache.gcs]  
  [runners.docker]  
    tls_verify = false  
    image = "docker:18.09.9"  
    privileged = false  
    disable_entrypoint_overwrite = false  
    oom_kill_disable = false  
    disable_cache = false  
    volumes = ["/var/run/docker.sock:/var/run/docker.sock","/root/.minikube:/root/.minikube","/root/.kube:/root/.kube", "/cache", "/data/.m2/:/root/.m2/"]  
    pull_policy = "if-not-present"  
    shm_size = 0  
- restart gitlab-runner    
docker restart gitlab-runner
- deploy gitlab group variable  
create group k8s-demo  
go to http://k8s-master/groups/k8s-demo/-/settings/ci_cd  
setting >> CI/CD >> DOCKER_HUB_REPO => k8s-master:5000/k8s-ci

## build local docker image
- k8s-master:5000/mvn3-jdk8-docker18  
>cd docker-images/ali-maven-docker  
docker build -t k8s-master:5000/mvn3-jdk8-docker18 .  
- k8s-master:5000/java8:alpine
>cd docker-images/java8-alpine  
docker build -t k8s-master:5000/java8:alpine .  
- k8s-master:5000/kubectl:1.19.3
>cd docker-images/kubectl  
docker build -t k8s-master:5000/kubectl:1.19.3 .  

## frequent shortcut cmd to kubectl for help
kubectl run nginx --image=docker.io/nginx:latest --port=80  
kubectl expose deployment hello-minikube --type=NodePort  

kubectl get deployment
>NAME      DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
nginx     1         1         1            1           7m

kubectl get pods
>NAME                     READY     STATUS    RESTARTS   AGE
nginx-2092755988-lzn67   1/1       Running   0          7m

kubectl port-forward nginx-2092755988-lzn67 80:80
>Forwarding from 127.0.0.1:80 -> 80
Forwarding from [::1]:80 -> 80

kubectl delete deployment nginx  
kubectl delete service nginx  

minikube stop  