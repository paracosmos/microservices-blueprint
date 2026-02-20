com.matoo.auth
├─ AuthApplication.kt
│
├─ adapter
│   ├─ in
│   │   └─ web
│   │       ├─ AuthController.kt
│   │       └─ dto
│   │           ├─ AuthResponse.kt
│   │           └─ OAuthRequest.kt
│   │
│   └─ out
│       ├─ client
│       │   └─ UserClientAdapter.kt
│       ├─ jwt
│       │   └─ JwtTokenProviderAdapter.kt
│       └─ oauth
│           ├─ OAuthClientAdapter.kt
│           ├─ apple
│           │   └─ AppleOAuthStrategyAdapter.kt
│           ├─ dto
│           │   └─ OAuthDtos.kt
│           └─ google
│               └─ GoogleOAuthStrategyAdapter.kt
│
├─ application
│   ├─ model
│   │   ├─ AuthRequestModel.kt
│   │   ├─ AuthResponseModel.kt
│   │   ├─ OAuthRequestModel.kt
│   │   └─ OAuthResponseModel.kt
│   ├─ port
│   │   ├─ in
│   │   │   └─ AuthUseCase.kt
│   │   └─ out
│   │       ├─ OAuthClientPort.kt
│   │       ├─ OAuthStrategyPort.kt
│   │       ├─ TokenProviderPort.kt
│   │       └─ UserClientPort.kt
│   └─ service
│       └─ AuthApplicationService.kt
│
├─ domain
│   ├─ model
│   │   ├─ OAuthUserInfo.kt
│   │   └─ TokenPair.kt
│   ├─ policy
│   └─ service
│       └─ AuthDomainService.kt
│
├─ infrastructure
    └─ config
        ├─ AuthModuleConfig.kt
        └─ client
            ├─ ExternalWebClientConfig.kt
            ├─ InternalWebClientConfig.kt
            └─ WebClientTracingConfig.kt
