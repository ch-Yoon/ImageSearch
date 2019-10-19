
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
- Disk Cache 구현
- 최근 검색 기록 구현
- Network 장애 시, RecyclerView 하단에 존재하는 FooterView를 통해 재시도 요청 구현

## 시연
### 검색 기능
- 사용자가 입력한 키워드로 검색

![search](https://user-images.githubusercontent.com/20294749/62585019-ed565d00-b8f1-11e9-9c5e-9843f801c3aa.gif)

### 상세 화면
- 상세 화면 구성 및 웹 이동 

![detailscreen](https://user-images.githubusercontent.com/20294749/62585016-ecbdc680-b8f1-11e9-8fcc-f37427c24a40.gif)

### Preload
- 한 줄에 노출되는 아이템의 갯수에 따라 프리로드 시점 조절
- ex) 한 줄에 4개 일때는 40개씩, 3개 일때는 30개씩, 2개 일때는 20

![preload](https://user-images.githubusercontent.com/20294749/62585018-ed565d00-b8f1-11e9-8b0c-f87e2740613b.gif)

### Network 장애 시 재시도 가능
- Network 장애 시, 스크롤 최하단에서 재시도 버튼을 통해 재시도 가능

![network_error_resolve](https://user-images.githubusercontent.com/20294749/62585015-ecbdc680-b8f1-11e9-8f09-4ebb16591621.gif)

### DiskCache 
- 검색한 내용은 Disk에 저장

![diskcache](https://user-images.githubusercontent.com/20294749/62585017-ed565d00-b8f1-11e9-9fcf-d8d9dfec9a9f.gif)

### 검색 기록
- 검색 기록 추가 삭제 가능

![recently_search_log](https://user-images.githubusercontent.com/20294749/62585014-ecbdc680-b8f1-11e9-8b93-115d632dbba1.gif)



## 개발 환경
```xml
complieSdkVersion : 28
minSdkVersion : 15
targetSdkVersion : 28
AndroidStudioVersion : 3.2.1
```


