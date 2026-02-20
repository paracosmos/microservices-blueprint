com.matoo.board
├─ BoardApplication.kt
│
├─ adapter
│   ├─ in
│   │   ├─ scheduler
│   │   ├─ stream
│   │   └─ web
│   │       ├─ BoardController.kt
│   │       ├─ BoardFileController.kt
│   │       ├─ BoardPublicController.kt
│   │       └─ dto
│   │           ├─ CommentCreateRequest.kt
│   │           ├─ PostCommentResponse.kt
│   │           └─ PostCreateRequest.kt
│   │
│   └─ out
│       ├─ PostAdapter.kt
│       ├─ client
│       │   ├─ S3StorageClient.kt
│       │   ├─ SupabaseStorageClient.kt
│       │   └─ dto
│       │       └─ SignedUploadResponse.kt
│       ├─ event
│       │   ├─ kafka
│       │   └─ redis
│       │       └─ RedisEventPublisher.kt
│       ├─ persistence
│       │   ├─ cache
│       │   │   ├─ PostCacheAdapter.kt
│       │   │   ├─ PostLocalCacheAdapter.kt
│       │   │   └─ PostRedisAdapter.kt
│       │   ├─ comment
│       │   │   ├─ CommentEntity.kt
│       │   │   ├─ CommentJpaRepository.kt
│       │   │   ├─ CommentMapper.kt
│       │   │   └─ CommentPersistenceAdapter.kt
│       │   ├─ event
│       │   ├─ file
│       │   │   ├─ FileEntity.kt
│       │   │   ├─ FileJpaRepository.kt
│       │   │   ├─ FileMapper.kt
│       │   │   └─ FilePersistenceAdapter.kt
│       │   └─ post
│       │       ├─ PostEntity.kt
│       │       ├─ PostJpaRepository.kt
│       │       ├─ PostMapper.kt
│       │       └─ PostRepositoryAdapter.kt
│       └─ storage
│           ├─ S3StorageAdapter.kt
│           └─ SupabaseStorageAdapter.kt
│
├─ application
│   ├─ model
│   │   ├─ FileInfo.kt
│   │   ├─ PresignCommand.kt
│   │   ├─ PresignResult.kt
│   │   ├─ StoredFile.kt
│   │   └─ UploadInstructions.kt
│   ├─ port
│   │   ├─ in
│   │   │   ├─ BoardQueryUseCase.kt
│   │   │   ├─ CommentUseCase.kt
│   │   │   ├─ FileUseCase.kt
│   │   │   └─ PostUseCase.kt
│   │   └─ out
│   │       ├─ CommentCommandPort.kt
│   │       ├─ CommentQueryPort.kt
│   │       ├─ PostCachePort.kt
│   │       ├─ PostCommandPort.kt
│   │       ├─ PostQueryPort.kt
│   │       └─ file
│   │           ├─ FileCommandPort.kt
│   │           ├─ FileQueryPort.kt
│   │           └─ FileStoragePort.kt
│   └─ service
│       ├─ BoardQueryService.kt
│       ├─ comment
│       │   └─ CommentService.kt
│       ├─ file
│       │   └─ FileService.kt
│       └─ post
│           └─ PostService.kt
│
├─ domain
│   ├─ model
│   │   ├─ Comment.kt
│   │   ├─ File.kt
│   │   ├─ FileStatus.kt
│   │   ├─ Post.kt
│   │   ├─ RelatedType.kt
│   │   └─ StorageProvider.kt
│   └─ service
│
└─ infrastructure
├─ cache
│   ├─ CacheConfig.kt
│   └─ RedisConfig.kt
├─ config
│   └─ BoardModuleConfig.kt
└─ storage
    ├─ S3ClientConfig.kt
    ├─ StorageConfig.kt
    ├─ StorageProperties.kt
    └─ SupabaseClientConfig.kt
