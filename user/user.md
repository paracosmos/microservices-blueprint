com.matoo.user
├─ UserApplication.kt
│
├─ adapter
│   ├─ in
│   │   ├─ api
│   │   │   └─ UserInternalController.kt
│   │   ├─ event
│   │   │   ├─ UserEventDispatcher.kt
│   │   │   ├─ UserEventListener.iml
│   │   │   ├─ kafka
│   │   │   └─ redis
│   │   │       ├─ UserRedisRedisEventListener.kt
│   │   │       └─ handler
│   │   │           └─ CommentCreatedHandler.kt
│   │   ├─ scheduler
│   │   └─ web
│   │       ├─ EmailController.kt
│   │       ├─ PushController.kt
│   │       ├─ TermsController.kt
│   │       ├─ UserController.kt
│   │       └─ dto
│   │           ├─ AgreeTermsRequest.kt
│   │           ├─ AgreeTermsResponse.kt
│   │           ├─ EmailRequest.kt
│   │           ├─ GoogleSignupRequest.kt
│   │           ├─ LocalSignupRequest.kt
│   │           ├─ PushSubscribeRequest.kt
│   │           ├─ RestoreRequest.kt
│   │           └─ TermsResponse.kt
│   │
│   └─ out
│       ├─ cache
│       │   └─ Key.kt
│       ├─ client
│       │   ├─ GmailSmtpEmailClientAdapter.kt
│       │   ├─ SesEmailClientAdapter.kt
│       │   └─ WebPushClientAdapter.kt
│       ├─ event
│       ├─ persistence
│       │   ├─ KeyRepositoryRedis.kt
│       │   ├─ provider
│       │   │   ├─ ProviderEntity.kt
│       │   │   ├─ ProviderKeyConverter.kt
│       │   │   └─ ProviderMapper.kt
│       │   ├─ push
│       │   │   ├─ PushSubscriptionEntity.kt
│       │   │   ├─ PushSubscriptionJpaRepository.kt
│       │   │   ├─ PushSubscriptionMapper.kt
│       │   │   └─ PushSubscriptionPersistenceAdapter.kt
│       │   ├─ terms
│       │   │   ├─ TermsEntity.kt
│       │   │   ├─ TermsJpaRepository.kt
│       │   │   ├─ TermsMapper.kt
│       │   │   ├─ TermsPersistenceAdapter.kt
│       │   │   └─ agreement
│       │   │       ├─ UserTermsAgreementEntity.kt
│       │   │       ├─ UserTermsAgreementId.kt
│       │   │       ├─ UserTermsAgreementJpaRepository.kt
│       │   │       ├─ UserTermsAgreementMapper.kt
│       │   │       └─ UserTermsAgreementPersistenceAdapter.kt
│       │   └─ user
│       │       ├─ UserEntity.kt
│       │       ├─ UserJpaRepository.kt
│       │       ├─ UserMapper.kt
│       │       └─ UserPersistenceAdapter.kt
│       └─ template
│           └─ ThymeleafTemplateRenderer.kt
│
├─ application
│   ├─ model
│   │   ├─ AgreeTermCommand.kt
│   │   ├─ GoogleSignupCommand.kt
│   │   ├─ LocalSigninCommand.kt
│   │   ├─ LocalSignupCommand.kt
│   │   ├─ OAuthSigninCommand.kt
│   │   ├─ PushSubscribeCommand.kt
│   │   └─ SigninQuery.kt
│   ├─ port
│   │   ├─ in
│   │   │   ├─ EmailUseCase.kt
│   │   │   ├─ PushUseCase.kt
│   │   │   ├─ TermsUseCase.kt
│   │   │   ├─ UserTermsUseCase.kt
│   │   │   └─ UserUseCase.kt
│   │   └─ out
│   │       ├─ EmailSenderPort.kt
│   │       ├─ PushSubscriptionCommandPort.kt
│   │       ├─ PushSubscriptionQueryPort.kt
│   │       ├─ TermsQueryPort.kt
│   │       ├─ UserCommandPort.kt
│   │       ├─ UserQueryPort.kt
│   │       ├─ UserTermsAgreementCommandPort.kt
│   │       ├─ UserTermsAgreementQueryPort.kt
│   │       └─ WebPushSenderPort.kt
│   └─ service
│       ├─ EmailApplicationService.kt
│       ├─ PushApplicationService.kt
│       ├─ TermsApplicationService.kt
│       ├─ UserApplicationService.kt
│       └─ UserTermsApplicationService.kt
│
├─ domain
│   ├─ model
│   │   ├─ notification
│   │   │   ├─ EmailMessage.kt
│   │   │   └─ PushSubscription.kt
│   │   ├─ provider
│   │   │   ├─ Provider.kt
│   │   │   └─ ProviderKey.kt
│   │   ├─ terms
│   │   │   ├─ Terms.kt
│   │   │   ├─ TermType.kt
│   │   │   └─ UserTermsAgreement.kt
│   │   └─ user
│   │       └─ User.kt
│   └─ service
│
├─ infrastructure
    ├─ config
    │   ├─ UserModuleConfig.kt
    │   ├─ email
    │   │   ├─ AwsSesConfig.kt
    │   │   ├─ AwsSesProperties.kt
    │   │   ├─ EmailConfig.kt
    │   │   └─ EmailProperties.kt
    │   ├─ event
    │   │   └─ UserTopicHandlersConfig.kt
    │   ├─ jpa
    │   │   └─ JpaAuditingConfig.kt
    │   ├─ redis
    │   │   ├─ AbstractRedisEventListener.kt
    │   │   └─ RedisConfig.kt
    │   └─ webpush
    │       ├─ WebPushConfig.kt
    │       └─ WebPushProperties.kt
    └─ logging
