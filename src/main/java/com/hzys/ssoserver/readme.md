## 密码模式 直接获取token
### /oauth/token
#### 参数为:username=admin&password=123456&client_id=client&client_secret=admin&grant_type=password
#### username 和password 为单个用户账号密码 可变
#### client_id 和 client_secret OAuth2 自己设置 --- AuthorizationServerConfig 中设置
###grant_type=password 密码模式
````json
{
	"access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDA5Mzc4NzMsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiJdLCJqdGkiOiIwNzdlOTMwMi03MTY3LTQ3OGItOTE3OC0wNTEwOTExZjFmYzAiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.PmBDNSNNDl9nycCxwhigGxjh2hQht1G2eNxlryBpkXk",
	"token_type": "bearer",
	"refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIwNzdlOTMwMi03MTY3LTQ3OGItOTE3OC0wNTEwOTExZjFmYzAiLCJleHAiOjE2MDA5Mzc4NzMsImF1dGhvcml0aWVzIjpbIlJPTEVfYWRtaW4iXSwianRpIjoiOWU0MWUzMDUtMGVlNy00YTA5LWE3ZDktYzFhMTAyMjlhODJhIiwiY2xpZW50X2lkIjoiY2xpZW50In0.p7oEARPEGBgnlR9GsLWy1S4r42lC0i4_YMekQSoAsVI",
	"expires_in": 50,
	"scope": "read write",
	"jti": "077e9302-7167-478b-9178-0510911f1fc0"
}
````

## 授权码模式
### /oauth/authorize
#### 参数为 client_id=client&response_type=code&scope=read&redirect_uri=http://localhost:9999/dd/code
#### 未登录则被转发到 login 登录接口 在login接口中可以处理一些逻辑 并且 转发至登录表单
#### 默认登录为 /login接口
### 再次请求原来得请求地址 跳转至请求地址中的重定向地址即redirect_uri的值
````json
{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDA5NTA1MjYsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiJdLCJqdGkiOiJkN2JlY2NkYS0zOTViLTQ2YjAtOWU2NC00ZDczMGQzMWNlNjAiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJzY29wZSI6WyJyZWFkIl19.ctIu4bUAhFKo8ThFxfTu_YNr0kdiIOlPQbazh8G6AnE","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwiYXRpIjoiZDdiZWNjZGEtMzk1Yi00NmIwLTllNjQtNGQ3MzBkMzFjZTYwIiwiZXhwIjoxNjAwOTUwNTI2LCJhdXRob3JpdGllcyI6WyJST0xFX2FkbWluIl0sImp0aSI6Ijg3NGZhMmNhLTNjZGUtNGE2My04MmMyLTdjNWQ5OWNlZjU0YSIsImNsaWVudF9pZCI6ImNsaWVudCJ9.WTsAMoPAE9YgH_FkqZtAVah0bRrbQr-5K1xi78vUqT0","expires_in":11999,"scope":"read","jti":"d7beccda-395b-46b0-9e64-4d730d31ce60"}
````

`````` java
    /**
      * 根据code 来获取 code
      * @param code Oauth 2.0 授权而来的code
      * @param request
      * @param response
      * @return token的一些参数的值
      */
     @RequestMapping("/dd/code")
     public String code(String code, HttpServletRequest request, HttpServletResponse response) {
         RestTemplate restTemplate = new RestTemplate();
         String access_token_url = "http://localhost:10000/oauth/token";
         access_token_url += "?client_id=client&code=" + code;
         access_token_url += "&grant_type=authorization_code";
         access_token_url += "&redirect_uri=http://localhost:9999/dd/code";
         access_token_url += "&client_secret=admin";
         System.out.println("access_token_url " + access_token_url);
         ResponseEntity<String> responseEntity = restTemplate.exchange(access_token_url, HttpMethod.POST, null, String.class);
         ObjectMapper mapper = new ObjectMapper();
         JsonNode node = null;
         try {
             node = mapper.readTree(responseEntity.getBody());
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
         String token = node.path("access_token").asText();
         System.out.println("access_token" +token);
         System.out.println( node.toString());
         return node.toString();
     }
``````

----
###根据上两种方式获取token之后的操作方式
`````` java
方式一:
http://localhost:10000/user/oauth/all?access_token=4fc443ca-4a59-4f45-9dad-67ed6c6f9872
方式二:
http://localhost:10000/user/oauth/all
请求头中
hearders:  Authorization:  bearer 4fc443ca-4a59-4f45-9dad-67ed6c6f9872
其中的bearer ===  上面获取token中的 token_type的值
其中 4fc443ca-4a59-4f45-9dad-67ed6c6f9872 为  上面获取token中的 access_token的值
``````


```text
1.启动两个项目三个程序 ssoserver(端口10000) 以及 ssoclient(端口9999和 9998)
3. 请求ssoclient 项目中的 中的 http://localhost:9999/client/member/info
第一次请求时 会跳转到  ssoserver 登录页面。 输入账户密码 ----- 账户随便输入,密码为123456
第二次请求 则会出现 你输入的 账户密码的一些数据 如下图
{
	"authorities": [{
		"authority": "ROLE_admin"
	}],
	"details": {
		"remoteAddress": "0:0:0:0:0:0:0:1",
		"sessionId": "323C6488DA3DF1ED2AAB9121BE167351",
		"tokenValue": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDIyMTY2NTksInVzZXJfbmFtZSI6InlhbmdzaWppbmciLCJhdXRob3JpdGllcyI6WyJST0xFX2FkbWluIl0sImp0aSI6IjgxMjRhYWRmLWQzMzgtNDQ3ZC05M2YzLTE4MDg3ZjRhYWE4NCIsImNsaWVudF9pZCI6ImNsaWVudCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.Vfz0eJdpHVGWovRz-cWXgnBnHr17lauznW-wAVdoLuI",
		"tokenType": "bearer",
		"decodedDetails": null
	},
	"authenticated": true,
	"userAuthentication": {
		"authorities": [{
			"authority": "ROLE_admin"
		}],
		"details": null,
		"authenticated": true,
		"principal": "yangsijing",
		"credentials": "N/A",
		"name": "yangsijing"
	},
	"oauth2Request": {
		"clientId": "client",
		"scope": ["read", "write"],
		"requestParameters": {
			"client_id": "client"
		},
		"resourceIds": [],
		"authorities": [],
		"approved": true,
		"refresh": false,
		"redirectUri": null,
		"responseTypes": [],
		"extensions": {},
		"grantType": null,
		"refreshTokenRequest": null
	},
	"clientOnly": false,
	"credentials": "",
	"principal": "yangsijing",
	"name": "yangsijing"
}
换个端口请求  http://localhost:9998/client/member/info 
同样也会出现上图 显示数据

退出操作
当执行任意端口的退出操作时 http://localhost:9998/client/logout 或者http://localhost:9999/client/logout
浏览器显示 {"code":200,"message":"退出成功"}
则 请求之前任意端口的 api 时则 跳转到登录页面

疑虑： 
1. 利用token来获取数据 但是token却不知道在哪里实现
2. sso配置却无法实现的个性化 比如: client-secret cline-id

```










































