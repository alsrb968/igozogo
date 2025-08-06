# 🏛️ Igozogo(이고조고)

한국의 아름다운 관광지와 그곳에 담긴 이야기를 오디오와 함께 탐험하는 Android 애플리케이션입니다.

## ✨ 주요 기능

- **🗺️ 관광지 탐색**: 한국관광공사 API를 통한 전국 관광지 정보 제공
- **📖 스토리텔링**: 각 관광지에 담긴 역사와 문화 이야기
- **🎵 오디오 가이드**: 장소별 오디오 콘텐츠로 몰입감 있는 경험
- **🔍 검색 & 필터링**: 카테고리별, 키워드별 관광지 검색
- **📍 위치 기반 서비스**: 현재 위치 주변 관광지 추천
- **⭐ 북마크**: 관심있는 장소 저장 및 관리

## 🎯 대상 사용자

- 한국 문화와 역사에 관심있는 관광객
- 새로운 여행지를 찾는 국내 여행자
- 오디오 가이드를 선호하는 사용자
- 깊이 있는 여행 경험을 원하는 모든 분들

## 🏗️ 아키텍처

이 프로젝트는 **Clean Architecture**와 **모듈화 설계**를 기반으로 구성되어
있으며, [Now in Android](https://github.com/android/nowinandroid) 프로젝트의 아키텍처 패턴을 참고했습니다.

### 📁 모듈 구조

```
igozogo/
├── app/                   # 메인 애플리케이션 모듈
├── core/
│   ├── model/             # 도메인 데이터 모델
│   ├── domain/            # 비즈니스 로직 (Respotory, UseCase)
│   ├── data/              # 데이터 레이어 (API, DB, Player)
│   ├── design/            # 공통 UI 컴포넌트 & 디자인 시스템
│   └── testing/           # 테스트 유틸리티 & 테스트 데이터
├── feature/
│   ├── home/              # 홈 화면
│   ├── search/            # 검색 기능
│   ├── bookmark/          # 북마크 관리
│   ├── placedetail/       # 관광지 상세 정보
│   ├── storydetail/       # 스토리 상세 정보
│   ├── player/            # 오디오 플레이어
│   └── setting/           # 설정
└── build-logic/           # 커스텀 Gradle 컨벤션 플러그인
```

### 🔧 기술 스택

| 분야         | 기술                                             |
|------------|------------------------------------------------|
| **UI**     | Jetpack Compose, Material 3                    |
| **아키텍처**   | Clean Architecture, MVI Pattern                |
| **의존성 주입** | Hilt                                           |
| **데이터베이스** | Room (with Paging 3)                           |
| **네트워킹**   | Retrofit, Gson                                 |
| **미디어**    | ExoPlayer (Media3)                             |
| **비동기 처리** | Kotlin Coroutines, Flow                        |
| **테스팅**    | JUnit, MockK, Turbine                          |
| **빌드**     | Gradle (Kotlin DSL), Custom Convention Plugins |

### 🎨 디자인 패턴

- **Repository Pattern**: 데이터 접근 추상화
- **Use Case Pattern**: 비즈니스 로직 캡슐화
- **MVI (Model-View-Intent)**: 단방향 데이터 흐름
- **Dependency Injection**: 모듈 간 결합도 최소화

## ⚙️ API 설정

`local.properties` 파일에 한국관광공사 API 서비스 키를 추가하세요:
```properties
tourapi.serviceKey=YOUR_TOUR_API_SERVICE_KEY
```

> 💡 API 키는 [한국관광공사 Tour API](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15101578)에서 발급받을 수 있습니다.

## 🏛️ 데이터 아키텍처

### 📡 API 통합

- **한국관광공사 Tour API**: 관광지 및 스토리 데이터
- **Odii API**: 관광 정보 다국어 서비스
- **지원 기능**: 목록 조회, 위치 기반 검색, 키워드 검색

### 🗄️ 로컬 데이터베이스

- **Room Database**: 오프라인 지원을 위한 로컬 캐싱
- **Paging 3**: 대용량 데이터 효율적 로딩
- **RemoteMediator**: 로컬/원격 데이터 동기화

### 🎵 오디오 시스템

- **ExoPlayer**: 고품질 오디오 재생
- **백그라운드 재생**: 미디어 세션을 통한 백그라운드 지원
- **재생 상태 관리**: 진행률, 반복 모드 등

## 🧪 테스트 전략

### 📊 테스트 구조

- **단위 테스트**: 비즈니스 로직 검증
- **UI 테스트**: Compose UI 및 ViewModel 테스트
- **통합 테스트**: 데이터베이스 및 API 연동 테스트

### 🎯 테스트 원칙

- **Given-When-Then** 패턴 사용
- 중앙화된 테스트 데이터 관리
- MockK를 활용한 의존성 모킹


## 🙏 감사의 말

- [한국관광공사](https://www.visitkorea.or.kr/) - 관광 데이터 제공
- [Now in Android](https://github.com/android/nowinandroid) - 아키텍처 참고
- [Material Design](https://material.io/) - 디자인 시스템

---

<p align="center">
  Made with ❤️ for Korean Tourism
</p>