
## 윤찬혁 - Kakao 이미지 검색 API 활용 이미지 검색 앱

![app_screen_shot](https://user-images.githubusercontent.com/20294749/62584271-bd598a80-b8ee-11e9-9c3b-c9edaf5191aa.png)

## apk file
- 다운로드 링크 -> [구글 드라이브](https://drive.google.com/drive/folders/1vkTsG283bOHdHUEd9cduHyEvTtt9Tydu?usp=sharing)

## 만약 설치가 안된다면?

![not_install_guide](https://user-images.githubusercontent.com/20294749/62586404-71abde80-b8f8-11e9-85d7-288e84418646.png)

## 개요
- 사용 언어 : Java
- 사용 패턴 및 기술 : MVVM + DataBinding + Retrofit2 + Room + RxJava2

## 구현 기능
### 기본 요구사항 구현
- 이미지 검색 API 활용 -> **(카카오 이미지 검색 API 활용)**
- List나 Grid로 구성 -> **(Grid로 구성)**
- 상세 화면 자유롭게 구성 -> **(상세 화면 구성 및 웹 이동 구성)**
- Toolbar에 이름 나오도록 구성 -> **(Toolbar 왼쪽 상단에 이름 노출)**
- 개발언어는 Java나 Kotlin으로 선택 -> **(Java 선택)**

### 추가 기능 구현
- Grid 사이즈 변경
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



## 개발 환경 및 사용 의존성
### 개발 환경
```xml
complieSdkVersion : 28
minSdkVersion : 15
targetSdkVersion : 28
AndroidStudioVersion : 3.2.1
```
### 의존성

```gradle
buildscript {
    ext.appcompat_version = '1.0.2'
    ext.constraintlayout_version = '1.1.3'
    ext.junit_version = '4.12'
    ext.runner_version = '1.2.0'
    ext.espresso_core_version = '3.2.0'
    ext.recycler_view_version = '1.0.0'
    ext.rxjava_version = '2.2.8'
    ext.rxandroid_version = '2.1.1'
    ext.retrofit_version = '2.4.0'
    ext.okhttp_version = '3.11.0'
    ext.glide_version = '4.9.0'
    ext.lifecycle_version = '2.0.0'
    ext.photo_view_version = '2.0.0'
    ext.room_version = '2.2.0-alpha01'
    
    ...
}
```
```gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "androidx.appcompat:appcompat:$appcompat_version"
    implementation "androidx.constraintlayout:constraintlayout:$constraintlayout_version"
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation "junit:junit:$junit_version"
    androidTestImplementation "androidx.test:runner:$runner_version"
    androidTestImplementation "androidx.test.espresso:espresso-core:$espresso_core_version"

    // recyclerView
    implementation "androidx.recyclerview:recyclerview:$recycler_view_version"

    // retrofit2
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.retrofit2:adapter-rxjava2:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    // rxJava
    implementation "io.reactivex.rxjava2:rxjava:$rxjava_version"
    implementation "io.reactivex.rxjava2:rxandroid:$rxandroid_version"

    // glide
    implementation "com.github.bumptech.glide:glide:$glide_version"
    annotationProcessor "com.github.bumptech.glide:compiler:$glide_version"

    // viweModel and liveData
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"

    // room
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-rxjava2:$room_version"

    // photo view
    implementation "com.github.chrisbanes:PhotoView:$photo_view_version"
}
```


