## Calry
- 반복되는 일정을 편하게 관리하고 일정을 공유하고 저장하는 캘린더 앱
- https://www.notion.so/Calry-b7ef1765fc074ad0a6fad57e5b271ec6


## Springboot-Calry
> Django로 구현했던 캘리 어플리케이션 서버에 대한 코드를 Spring boot를 활용하여 구조 및 API를 다시 구성하고, 같은 논리구조의 경우에는 리팩토링 하는 작업을 진행하며 학습하고 있습니다.

- Calry 어플에 구현되어있는 추천 서비스에 대한 지속적인 업데이트가 시간적으로 힘들다고 판단하여 기능을 없애기로 했습니다.
- 동시에 커뮤니티적인 성격을 줄이고, 사람들의 자기개발을 위한 루틴에 대한 니즈와 필요성에 대해서 파악하여 해당 기능을 더 키우려고 했습니다.
- Django로 다시 수정하는 방법도 있었지만 Java와 SpringBoot에 대한 관심 증가로 개발 언어와 프레임워크를 변경해보고 싶었습니다.  

## Project Structure
> Spring Boot(API Server) + Flutter(SPA) 구조로 개발했습니다. 두가지 모두 담당하여 독학으로 개발하며, 학습하고 있습니다.

> 사용중인 기술은 다음과 같습니다.
- Spring Boot
- Spring Security
- Postgresql 
- JPA & QueryDSL
- JWT


### 데이터 구조 ERD(erdcloud)
- https://www.erdcloud.com/d/JrTwNyxMsg6KLSZ5J

<img width="917" alt="springboot-calry-ERD" src="https://user-images.githubusercontent.com/78459713/139792302-085dddd3-c739-4e25-910f-8d8be699a83a.png">

### API 문서(Postman)
- https://documenter.getpostman.com/view/10904197/UVByJAJm

## Spring Boot (API Server)
> React(SPA)에서 요청한 데이터를 JSON으로 response 합니다. 구조는 다음과 같습니다.

- component: project component를 관리
- config: project configuration을 관리
- exception: custom exception message를 관리
- controller: API를 관리
- domain: Entity Model과 repository(JPA/QueryDSL) 관리
- dto : request/response dto를 관리
- service: controller에서 호출된 것을 로직에 따라서 리턴

## Spring Security (Security)
> Security 설정을 추가해 인가된 사용자만 특정 API에 접근할 수 있도록 제한합니다.

- AuthenticationEntryPoint: JwtAuthenticationEntryPoint
- Token Authentication Filter : JwtAuthenticationFilter

## JWT(Login)
> JWT Token을 사용해 Authorizaton Header 기반 인증 시스템을 구현합니다. 구조는 다음과 같습니다.

![JWT](https://user-images.githubusercontent.com/78459713/139792333-85a249b2-d0f6-43a1-8cae-fa73b4a1dca1.jpg)


## 사용방법 및 기능 소개
### 1. 로그인
> 이메일과 비밀번호로 회원가입을 진행하며, 로그인 할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139793936-0804459d-3a4f-4aa1-aed0-2f4052021176.png">

### 2. 홈
> 오늘 진행할 루틴과 일정을 한눈에 확인할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139794628-efb38fb1-8e2b-4f08-92a3-c7b7cc12eb7e.png">

### 3. 월 달력 일정 확인
> 월 달력으로 저장되 일정을 한눈에 파악할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139794587-fe9ab010-9594-4e47-9df6-9af5e47d915c.png">


### 4. 오늘 진행할 루틴
> 미리 저장해놓은 사용자 루틴 그룹과, 사용자 루틴을 이용해서 오늘 진행할 루틴을 추가하며, 루틴 달성을 관리할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139794601-9284e55e-2c51-4bc6-a95e-b1bfe77bd69e.png">  |  <img width="300" src="https://user-images.githubusercontent.com/78459713/139795843-69ad7617-fc98-43c8-8097-6256a8e57d0c.png">


### 5. 일정 관리
> 시간과 내용을 포함하여 일정을 추가할 수 있으며, 미리 등록해놓은 빠른 일정으로 반복되는 일정을 관리할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139795849-6a5e5e58-78ac-4b30-9c62-03168d01dcd2.png">  |  <img width="300" src="https://user-images.githubusercontent.com/78459713/139794593-6a8562a3-0c1f-48c6-b011-64d4f78c4e9d.png">

### 6. 루틴 달성률
> 매일 진행했던 루틴에 대해서 계획에 세운 루틴과, 성공한 루틴을 중심으로 하여, 평소 달성률을 체크할 수 있습니다.

<img width="300" src="https://user-images.githubusercontent.com/78459713/139794140-54033b35-2956-4308-b9ee-4bee944a4ec0.png">
