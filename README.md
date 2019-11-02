
## Kakao 이미지 검색 API 활용 이미지 검색 앱

![app_demo](https://user-images.githubusercontent.com/20294749/67322924-b5ec6980-f54c-11e9-96f3-f2394375aab2.png)

## 개요
- Kotlin version
  - [Kotlin + Koin + RxJava + MVVM + LiveData + Retrofit2 + Room + UnitTest(mockk)](https://github.com/ch-Yoon/ImageSearch/tree/kotlin-rx-mvvm-retrofit2-room)
- Java version
  - [Java + RxJava + MVVM + LiveData + Retrofit2 + Room + UnitTest(mockto, powermock)](https://github.com/ch-Yoon/ImageSearch/tree/java-rx-mvvm-retrofit2-room)

## 구현 기능
- 카카오 이미지 검색 API를 활용하여 검색한 Grid로 구성
- 정렬 타입(정확도, 최신) 및 Grid Size 변경 가능
- Preload 구현
- 최근 검색 기록 구현
- Network 장애 시, RecyclerView FooterView를 통한 재시도 요청 구현

## 개발 환경
```xml
complieSdkVersion : 28
minSdkVersion : 24
targetSdkVersion : 28
```


