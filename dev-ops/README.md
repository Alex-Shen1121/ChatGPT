## Dev-Ops

### nginx 配置

本地配置文件映射
```shell
docker container cp Nginx:/etc/nginx/nginx.conf /Users/alex_shen/Projects/Chatgpt微服务应用体系/dev-ops/nginx/conf

docker container cp Nginx:/etc/nginx/conf.d/default.conf /Users/alex_shen/Projects/Chatgpt微服务应用体系/dev-ops/nginx/conf/conf.d

docker container cp Nginx:/usr/share/nginx/html/index.html /Users/alex_shen/Projects/Chatgpt微服务应用体系/dev-ops/nginx/html
```
启动 nginx
```shell
docker run \
--name Nginx \
-p 80:80 \
-v /Users/alex_shen/Projects/Chatgpt微服务应用体系/chatgpt/dev-ops/nginx/logs:/var/log/nginx \
-v /Users/alex_shen/Projects/Chatgpt微服务应用体系/chatgpt/dev-ops/nginx/html:/usr/share/nginx/html \
-v /Users/alex_shen/Projects/Chatgpt微服务应用体系/chatgpt/dev-ops/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /Users/alex_shen/Projects/Chatgpt微服务应用体系/chatgpt/dev-ops/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /Users/alex_shen/Projects/Chatgpt微服务应用体系/chatgpt/dev-ops/nginx/ssl:/etc/nginx/ssl/ \
--privileged=true \
-d \
--restart=always \
nginx


```
