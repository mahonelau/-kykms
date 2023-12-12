FROM nginx:1.24.0
MAINTAINER hnliuwx@gmail.com

RUN rm -f /etc/nginx/conf.d/default.conf
ADD ./dist /usr/share/nginx/kykms-root/

EXPOSE 80
