# chatgpt-data | DDD 工程分层架构

## 测试脚本

### 1. 验证码

```java
curl -X POST \
 http://localhost:8099/api/v1/auth/gen/code \
-H 'Content-Type: application/x-www-form-urlencoded' \
-d 'openid=scy'
```

- 也可以通过启动本地 natapp 内网穿透，对接公众号进行获取验证码

### 2. 登录 - 获取 Token

```java
curl -X POST \
http://localhost:8099/api/v1/auth/login \
-H 'Content-Type: application/x-www-form-urlencoded' \
-d 'code=2610'
```

- 登录后可以获取 Token

### 3. 功能 - 流式问题

```java
curl -X POST \
http://localhost:8099/api/v1/chat/completions \
-H 'Content-Type: application/json;charset=utf-8' \
-H 'Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzY3kiLCJvcGVuSWQiOiJzY3kiLCJleHAiOjE3MDU2MDEyNTEsImlhdCI6MTcwNDk5NjQ1MSwianRpIjoiNDU2N2MyZWItMzZiYi00ZjZmLWI5ODItODZjMjA0ODM2ZTIzIn0.W1Ukz1OlNCl6LIdXDWL0LD7WQZfSxYsGuvdIwEcGtQo' \
-d '{
"messages": [
{
"content": "1+1",
"role": "user"
}
],
"model": "gpt-3.5-turbo"
}'
```

- Token 是通过登录从控制台复制的，注意可别复制错了。

### 4. 查询商品列表

```java
curl -X GET \
-H "Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzY3kiLCJvcGVuSWQiOiJzY3kiLCJleHAiOjE3MDQ5MTYxOTEsImlhdCI6MTcwNDMxMTM5MSwianRpIjoiYjRlZmJiMDMtNDEyZi00ZTAzLTk4N2MtM2IxMDkxMDQ4MTA0In0.JbvdcKqk_uTjnVG2xe0E9Y0QVGdu7iTMsVUywTEQcTY" \
-H "Content-Type: application/x-www-form-urlencoded" \
http://localhost:8099/api/v1/sale/query_product_list
```

### 5. 用户下单商品

```java
curl -X POST \
-H "Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzY3kiLCJvcGVuSWQiOiJzY3kiLCJleHAiOjE3MDQ5MTYxOTEsImlhdCI6MTcwNDMxMTM5MSwianRpIjoiYjRlZmJiMDMtNDEyZi00ZTAzLTk4N2MtM2IxMDkxMDQ4MTA0In0.JbvdcKqk_uTjnVG2xe0E9Y0QVGdu7iTMsVUywTEQcTY" \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "productId=1001" \
http://localhost:8099/api/v1/sale/create_pay_order
```

