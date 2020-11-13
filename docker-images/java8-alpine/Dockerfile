FROM alpine
ENV LANG='zh_CN.UTF-8'

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk update \
    && apk --no-cache add openjdk8 curl busybox tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \ 
    && echo Asia/Shanghai > /etc/timezone \
    && apk del tzdata \
    && rm -rf /tmp/* /var/cache/apk/*
