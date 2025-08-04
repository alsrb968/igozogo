# Igozogo

Igozogo는 한국 관광 콘텐츠를 탐색하는 Android 애플리케이션으로, 장소, 스토리, 오디오 플레이어 기능을 제공합니다. 이 앱은 공유 기능을 위한 core 모듈과 특정 UI 컴포넌트를 위한 feature 모듈을 사용하는 모듈형 Clean Architecture 접근 방식을 사용합니다.

## 아키텍처

### 모듈 구조
- **core/model**: 순수 Kotlin 데이터 모델 (Place, Story, PlayerProgress, PlaybackState, RepeatMode)
- **core/domain**: 리포지토리와 유스케이스가 포함된 비즈니스 로직 레이어
- **core/data**: Room 데이터베이스, Retrofit API, 리포지토리 구현, DI 모듈이 포함된 데이터 레이어
- **core/design**: 공유 UI 컴포넌트 및 디자인 시스템
- **core/testing**: 테스트 유틸리티 및 공통 테스트 데이터
- **feature/***: 기능별 UI 모듈 (home, search, bookmark, setting, placedetail, storydetail, player)
- **app**: 네비게이션과 `@HiltAndroidApp` 설정이 포함된 메인 애플리케이션 모듈

### 주요 의존성
- **UI**: Material 3를 사용한 Jetpack Compose
- **DI**: 의존성 주입을 위한 Hilt
- **Database**: 페이징 지원이 포함된 Room
- **Network**: API 통신을 위한 Retrofit + Gson
- **Media**: 오디오 재생을 위한 ExoPlayer (Media3)
- **Testing**: 코루틴 테스트를 위한 JUnit, MockK, Turbine

## 빌드 시스템

프로젝트는 일관된 구성을 위해 `build-logic/convention/`에 위치한 커스텀 Gradle convention 플러그인을 사용합니다:
- `igozogo.android.application` - 메인 앱 구성
- `igozogo.android.library` - 라이브러리 모듈 설정
- `igozogo.android.feature` - 표준 의존성을 포함한 기능 모듈
- `igozogo.android.library.compose` - Compose가 활성화된 라이브러리
- `igozogo.android.hilt` - Hilt 의존성 주입
- `igozogo.android.room` - Room 데이터베이스 구성
- `igozogo.android.test` - 테스트 구성

## 주요 명령어

### 빌드 및 테스트
```bash
# 전체 프로젝트 빌드
./gradlew build

# 단위 테스트 실행
./gradlew test

# 특정 모듈 테스트 실행
./gradlew :core:domain:test
./gradlew :feature:home:testDebugUnitTest

# Android 인스트루먼테이션 테스트 실행
./gradlew connectedAndroidTest

# 린트 체크
./gradlew lint
./gradlew lintDebug

# 디바이스에 디버그 빌드 설치
./gradlew installDebug
```

### 개발 작업
```bash
# 클린 빌드
./gradlew clean

# 의존성 확인
./gradlew dependencies

# 디버깅용 소스 생성
./gradlew compileDebugSources
```

## 데이터 레이어 아키텍처

### API 통합
- **Tour API**: Visit Korea API를 통한 한국 관광 데이터
- **구성**: API 키는 `local.properties`에 `tourapi.serviceKey`로 저장
- **모델**: `core/data/model/remote/`의 원격 응답을 도메인 모델로 매핑

### 데이터베이스 스키마
- **Room Database**: Story, Theme, 페이징 키에 대한 엔티티가 포함된 `VisitKoreaDatabase`
- **Paging**: 원활한 로컬/원격 데이터 로딩을 위한 `RemoteMediator` 패턴 사용
- **Converters**: `core/data/db/converter/`의 복잡한 데이터 타입을 위한 커스텀 타입 컨버터

### 리포지토리 패턴
- `core/domain/repository/`의 도메인 인터페이스
- `core/data/repository/`의 구현체
- 복잡한 비즈니스 로직을 위한 `core/domain/usecase/`의 유스케이스

### 의존성 주입
- **Hilt 모듈**: 모든 DI 구성이 `core/data/di/`에 위치
  - `ApiModule`: Retrofit API 인스턴스 제공
  - `DatabaseModule`: Room 데이터베이스 및 DAO 제공  
  - `DataSourceModule`: 로컬/원격 데이터소스 제공
  - `RepositoryModule`: 리포지토리 구현체를 도메인 인터페이스에 바인딩
  - `PlayerModule`: ExoPlayer 인스턴스 제공
- **Application**: `app` 모듈에서 `@HiltAndroidApp` 어노테이션만 설정

## 테스트 전략

### 테스트 데이터
- `core/testing/data/`의 중앙화된 테스트 데이터 (PlaceTestData, StoryTestData 등)
- `core/testing/util/`의 공통 테스트 유틸리티

### 테스트 접근법
- **단위 테스트**: 도메인 및 데이터 레이어의 비즈니스 로직
- **UI 테스트**: 코루틴 및 StateFlow를 사용한 ViewModel 테스트
- **통합 테스트**: Room 테스트 유틸리티를 사용한 데이터베이스 작업

## 기능 모듈 규칙

### 구조
- 각 기능 모듈은 Screen + ViewModel + Navigation 패턴을 따름
- 타입 세이프 라우트를 사용하여 `navigation/` 패키지에 네비게이션 정의
- ViewModel은 상태 관리를 위해 StateFlow와 함께 Hilt 주입 사용

### 의존성
기능 모듈은 자동으로 다음을 포함:
- Core domain, design, testing, model 모듈
- Compose 의존성 (Activity, Animation, Navigation 등)
- ViewModel 주입을 위한 Hilt navigation compose
- 페이지네이션된 리스트를 위한 Paging Compose

## 플레이어 아키텍처

오디오 플레이어는 다음과 함께 ExoPlayer (Media3)를 사용:
- **PlayerDataSource**: 미디어 세션 관리
- **PlayerProgress**: 재생 상태 추적
- **RepeatMode**: 재생 모드 제어
- **UI Components**: 다양한 컨텍스트를 위한 PlayerMiniBar 및 PlayerBottomSheet

## API 구성

`local.properties`에 필요:
```properties
tourapi.serviceKey=YOUR_TOUR_API_SERVICE_KEY
```

서비스 키는 API 요청을 위해 자동으로 BuildConfig에 주입됩니다.