# CLAUDE.md

이 파일은 Claude Code (claude.ai/code)가 이 저장소에서 작업할 때 필요한 가이드를 제공합니다.

## 프로젝트 개요
Igozogo는 Clean Architecture를 사용하여 한국 관광 콘텐츠를 탐색하는 모듈형 Android 애플리케이션입니다. 이 앱은 Jetpack Compose와 Material 3를 사용하여 장소, 스토리, 오디오 플레이어 기능을 제공합니다.

### 프로젝트 참고 사항
- https://github.com/android/nowinandroid 링크의 Now In Android 프로젝트를 참고한 프로젝트임.

## 빌드 시스템 및 명령어

### 필수 개발 명령어
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

# 디버그 빌드 설치
./gradlew installDebug

# 클린 빌드
./gradlew clean
```

### 커스텀 Convention 플러그인
프로젝트는 일관된 구성을 위해 `build-logic/convention/`에 위치한 커스텀 Gradle convention 플러그인을 사용합니다:
- `igozogo.android.application` - 메인 앱 구성
- `igozogo.android.library` - 라이브러리 모듈 설정
- `igozogo.android.feature` - 표준 의존성을 포함한 기능 모듈
- `igozogo.android.library.compose` - Compose가 활성화된 라이브러리
- `igozogo.android.hilt` - Hilt 의존성 주입
- `igozogo.android.room` - Room 데이터베이스 구성
- `igozogo.android.test` - 테스트 구성

## 아키텍처 개요

### 모듈 구조
- **core/model**: 순수 Kotlin 데이터 모델 (Place, Story, PlayerProgress, PlaybackState, RepeatMode)
- **core/domain**: 리포지토리와 유스케이스가 포함된 비즈니스 로직 레이어
- **core/data**: Room 데이터베이스, Retrofit API, 리포지토리 구현, DI 모듈이 포함된 데이터 레이어
- **core/design**: Material 3를 사용한 공유 UI 컴포넌트 및 디자인 시스템
- **core/testing**: 테스트 유틸리티 및 공통 테스트 데이터
- **feature/\***: 기능별 UI 모듈 (home, search, bookmark, setting, placedetail, storydetail, player)
- **app**: 네비게이션과 `@HiltAndroidApp` 설정이 포함된 메인 애플리케이션 모듈

### 주요 아키텍처 패턴

#### 의존성 주입 (Hilt)
모든 DI 구성이 `core/data/di/`에 중앙화되어 있습니다:
- `ApiModule`: Retrofit API 인스턴스 제공
- `DatabaseModule`: Room 데이터베이스 및 DAO 제공
- `DataSourceModule`: 로컬/원격 데이터소스 제공
- `RepositoryModule`: 리포지토리 구현체를 도메인 인터페이스에 바인딩
- `PlayerModule`: ExoPlayer 인스턴스 제공

#### 데이터 레이어 아키텍처
- **Repository 패턴**: `core/domain/repository/`의 도메인 인터페이스, `core/data/repository/`의 구현체
- **Use Cases**: `core/domain/usecase/`의 복잡한 비즈니스 로직
- **데이터 매핑**: `core/data/mapper/`의 매퍼를 통해 원격 응답을 도메인 모델로 변환
- **Room Database**: Story, Theme, 페이징 키에 대한 엔티티가 포함된 `VisitKoreaDatabase`
- **Paging**: 원활한 로컬/원격 데이터 로딩을 위한 `RemoteMediator` 패턴 사용

#### 기능 모듈 패턴
각 기능 모듈은 Screen + ViewModel + Navigation 구조를 따릅니다:
- ViewModel은 상태 관리를 위해 StateFlow와 함께 Hilt 주입 사용
- 타입 세이프 라우트를 사용하여 `navigation/` 패키지에 네비게이션 정의
- State/Action/Effect sealed interface를 사용한 MVI 스타일 아키텍처

### API 구성
Visit Korea API의 관광 데이터를 사용하려면 `local.properties`에 구성이 필요합니다:
```properties
tourapi.serviceKey=YOUR_TOUR_API_SERVICE_KEY
```

### 플레이어 아키텍처
오디오 플레이어는 ExoPlayer (Media3)를 사용합니다:
- **PlayerDataSource**: 미디어 세션 관리
- **PlayerProgress**: 재생 상태 추적
- **RepeatMode**: 재생 모드 제어
- **UI Components**: 다양한 컨텍스트를 위한 PlayerMiniBar 및 PlayerBottomSheet

## 테스트 전략

### 테스트 데이터 및 유틸리티
- `core/testing/data/`의 중앙화된 테스트 데이터 (PlaceTestData, StoryTestData 등)
- `core/testing/util/`의 공통 테스트 유틸리티
- 코루틴 테스트를 위한 `MainDispatcherRule`

### 테스트 접근법
- **단위 테스트**: 도메인 및 데이터 레이어의 비즈니스 로직
- **UI 테스트**: 코루틴 및 StateFlow를 사용한 ViewModel 테스트
- **통합 테스트**: Room 테스트 유틸리티를 사용한 데이터베이스 작업
- **모킹**: 의존성을 위한 MockK, Flow 테스트를 위한 Turbine

### 테스트 명명 규칙
- 테스트 코드 작성 시 함수 이름은 Given-When-Then 패턴으로 한다.

## 기술 스택
- **UI**: Material 3를 사용한 Jetpack Compose
- **DI**: Hilt
- **Database**: 페이징 지원이 포함된 Room
- **Network**: Retrofit + Gson
- **Media**: ExoPlayer (Media3)
- **Testing**: 코루틴 테스트를 위한 JUnit, MockK, Turbine
- **Build**: Kotlin DSL을 사용한 커스텀 Gradle convention 플러그인