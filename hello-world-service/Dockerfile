FROM k8s-master:5000/java8:alpine
ADD /target/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]