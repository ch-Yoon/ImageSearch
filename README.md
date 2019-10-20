
## 윤찬혁 - Kakao 이미지 검색 API 활용 이미지 검색 앱

![app_screen_shot](https://user-images.githubusercontent.com/20294749/62584271-bd598a80-b8ee-11e9-9c3b-c9edaf5191aa.png)

## apk file
- 다운로드 링크 -> [구글 드라이브](https://drive.google.com/drive/folders/1vkTsG283bOHdHUEd9cduHyEvTtt9Tydu?usp=sharing)

## 만약 설치가 안된다면?

![not_install_guide](https://user-images.githubusercontent.com/20294749/62586404-71abde80-b8f8-11e9-85d7-288e84418646.png)

## 개요
- 사용 언어
  - Java
  
- 사용 기술
  - MVVM 
  - LiveData 
  - DataBinding
  - RxJava2
  - Retrofit2
  - Room
  - Unit Test(Mockito, Powermock)

## 구현 기능
### 기본 요구사항 구현
- 이미지 검색 API 활용 -> **(카카오 이미지 검색 API 활용)**
- List나 Grid로 구성 -> **(Grid로 구성)**
- 상세 화면 자유롭게 구성 -> **(상세 화면 구성 및 웹 이동 구성)**
- Toolbar에 이름 나오도록 구성 -> **(Toolbar 왼쪽 상단에 이름 노출)**
- 개발언어는 Java나 Kotlin으로 선택 -> **(Java 선택)**

### 추가 기능 구현
- Grid 사이즈 변경 가능하도록 구현
- Preload 구현
- 최근 검색 기록 구현
- Network 장애 시, RecyclerView 하단에 존재하는 FooterView를 통해 재시도 요청 구현

## 개발 환경
```xml
complieSdkVersion : 28
minSdkVersion : 15
targetSdkVersion : 28
AndroidStudioVersion : 3.2.1
```


