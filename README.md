# 🛤️ 공유 포토북 앱 Recoder

<br>

## 📋 프로젝트 소개

- ✈️ 친구, 연인 또는 가족끼리 추억을 회상하는 추억 공유 앨범 앱입니다.
- 💑 특정 사람들과 어떤 이벤트가 있었는지 시간 순으로 볼 수 있습니다.
- 📷 이벤트마다 공유 앨범으로 친구와 사진을 쉽게 공유할 수 있습니다.

<br>

## 👨‍👨‍👧‍👦 팀원 구성

| 이름 | Github |
| --- | --- |
| 👦 김명준 | [Mangjun](https://github.com/Mangjun) |
| 👦 김남규 | [namgue](https://github.com/namgue) |
| 👦 윤형식 | [hs010304](https://github.com/hs010304) |
| 👦 이동현 | [LEEDDONG](https://github.com/LEEDDONG) |
| 👦 전경섭 | [JKS8520](https://github.com/JKS8520) |
| 👧 조서윤 | [ewbrdjf](https://github.com/ewbrdjf) |

<br>

## 📃 개발 환경 및 개발 기간

### 🛠️ 개발 환경

- Front : Android Studio(Iguana)
- Back-end : Spring Boot(3.2.5)
- 버전 및 이슈 관리 : Git, Github
- 협업 툴 : Notion

### 📅 개발 기간

- 전체 개발 기간 : 2024.04.30 ~ 2024.06.03
- UI 구현 : 2024.04.30 ~ 2024.05.26
- 기능 구현 : 2024.04.30 ~ 2024.06.03

### 🖊️ 브랜치 전략

- **main** : 배포할 때만 사용하는 브랜치입니다.
- **develop** : 개발 단계에서 main 역할을 하는 브랜치입니다.
- **feature** : 하나의 기능을 개발하는 브랜치입니다.

<br>

## 🧩 역할 분담

| 이름 | 역할 |
| :---: | --- |
| 👨‍🎓<br>김명준 | 도메인 및 테이블 설계, 서버 Rest API 개발, 사진 관련 로직 개발, 갤러리, 사진 페이지 기능 개발, 네트워크 연결 관리 |
| 👨‍🎓<br>김남규 | 이벤트 관련 로직 개발, 이벤트 페이지 친구 추가 기능 개발, 일정 관리 |
| 👨‍🎓<br>윤형식 | 로그인, 회원가입 페이지 레이아웃 및 기능 개발, 문서 작업 |
| 👨‍🎓<br>이동현 | 회원 관련 로직 개발, 친구 페이지 기능 개발, 회원가입 페이지 중복 검사 기능 개발 |
| 👨‍🎓<br>전경섭 | 메인, 이벤트, 회원 정보 수정 페이지 기능 동작 |
| 👩‍🎓<br>조서윤 | 전체적인 UI 디자인 및 로그인, 회원가입 제외 모든 레이아웃 작성, PPT 제작 |

<br>

## 💡 기능 목록

* 회원 기능
    * 회원 가입
    * 로그인 / 로그아웃
    * 회원 정보 수정
    * 친구 추가 및 삭제

* 이벤트 기능
    * 이벤트 생성
    * 이벤트 삭제
    * 이벤트 수정
    * 이벤트 조회
    * 친구 초대

* 사진 기능
    * 사진 업로드
    * 사진 삭제
    * 사진 조회

* 기타 요구사항
    * 사진은 서버에 저장한다.
    * 로그인을 하면 앱을 껏다가 다시 켜도 유지되어야 한다.

<br>

## 도메인 모델과 테이블

### 도메인 설계
![Domain](https://github.com/HGCQ/AndroidProjects/blob/main/Image/%EB%8F%84%EB%A9%94%EC%9D%B8%20%EC%84%A4%EA%B3%84.png)

<br>

### 엔티티 분석
![Entity](https://github.com/HGCQ/AndroidProjects/blob/main/Image/%EC%97%94%ED%8B%B0%ED%8B%B0%20%EB%B6%84%EC%84%9D.png)

<br>

### 테이블 설계
![Table](https://github.com/HGCQ/AndroidProjects/blob/main/Image/%ED%85%8C%EC%9D%B4%EB%B8%94%20%EC%84%A4%EA%B3%84.png)

<br>

## 🔧 페이지별 기능

### Loading Page
![Loading](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EB%A1%9C%EB%94%A9%ED%99%94%EB%A9%B4.png)

<br>

설명: 앱 실행 후 처음 나오는 로딩화면   
이미지를 클릭하면 **만약 로그인 되어있다면,** 메인 페이지로 이동, **아니라면** 로그인 페이지로 이동된다.

### Login Page
![Login](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%99%94%EB%A9%B4.png)

<br>

설명: 로그인 화면   
이메일과 패스워드로 로그인 가능하며, 입력 후 Login 버튼을 누르면 로그인되고 메인 페이지로 이동, Join 버튼을 누르면 회원가입 페이지로 이동된다.

### Join Page
![Join](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85%ED%99%94%EB%A9%B4.png)

<br>

설명: 회원가입 화면   
닉네임, 이메일, 비밀번호를 입력해 버튼을 누르면 회원가입이 된 후 로그인 페이지로 이동된다.   
닉네임과 이메일은 중복 검사를 해야 한다.   

<br>

### Main Page
![Main1](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EB%A9%94%EB%89%B4%ED%99%94%EB%A9%B41.png)

<br>

![Main2](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EB%A9%94%EB%89%B4%ED%99%94%EB%A9%B42.png)

<br>

![Main3](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EB%A9%94%EB%89%B4%ED%99%94%EB%A9%B43.png)

<br>

설명: 메인 화면   
이벤트를 날짜 또는 이름으로 검색할 수 있고 친구 페이지와, 회원 정보 수정 페이지, 로그아웃을 할 수 있다.   
이벤트는 날짜 순으로 정렬되어 있으며, 클릭할 시 이벤트 페이지로 이동된다.   
밑에 있는 추가 버튼을 누르면 이벤트 생성 페이지로 이동된다.   

<br>

### Friend Page
![]()

<br>

설명: 친구 화면   

<br>

### Modify Page
![]()

<br>

설명: 회원 정보 수정 화면   

<br>

### Event Create Page
![EventCreate](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EC%9D%B4%EB%B2%A4%ED%8A%B8%EC%83%9D%EC%84%B1%ED%99%94%EB%A9%B4.png)

<br>

설명: 이벤트 생성 화면   
이벤트 이름, 이벤트 날짜, 이벤트 설명으로 이벤트를 생성할 수 있으며, 이미 만든 날짜로는 이벤트 생성이 불가능하다.   
뒤로 가기 버튼을 누르면 메인 페이지로 이동된다.

<br>

### Event Page
![]()

<br>

설명: 이벤트 화면   

<br>

### Gallery Page
![Gallery1](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EA%B0%A4%EB%9F%AC%EB%A6%AC%ED%99%94%EB%A9%B41.png)

<br>

![Gallery2](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EA%B0%A4%EB%9F%AC%EB%A6%AC%ED%99%94%EB%A9%B42.png)

<br>

설명: 갤러리 화면   
이벤트에 있는 사진들을 모두 볼 수 있는 페이지이고, 사진 클릭 시 사진 페이지로 이동된다.

<br>

### Photo Page
![Photo1](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EC%82%AC%EC%A7%84%ED%99%94%EB%A9%B41.png)

<br>

![Photo2](https://github.com/HGCQ/AndroidProjects/blob/develop/Image/%EC%82%AC%EC%A7%84%ED%99%94%EB%A9%B42.png)

<br>

설명: 사진 화면   
이벤트에 있는 사진들을 하나씩 크게 볼 수 있는 페이지이고, 좌우로 슬라이드 형식으로 넘길 수 있다.   
삭제 버튼을 누르면 현재 보고 있는 사진이 삭제되며, 뒤로 가기 버튼을 누르면 갤러리 페이지로 이동된다.

<br>

## 프로젝트 후기

### 👦 김명준

<br>

### 👦 김남규

<br>

### 👦 윤형식

<br>

### 👦 이동현

<br>

### 👦 전경섭

<br>

### 👧 조서윤
