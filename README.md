# ToDo API (Spring Boot 3)

할 일(ToDo) API입니다.  
기본 CRUD 이외에 완료 상태 일괄 리셋 API를 제공합니다.

- 수동 트리거: `POST /todos/reset-completed` → 완료된 항목을 모두 미완료로 변경
- 자동 실행(배치): 매일 00:00 (Asia/Seoul) 스케줄러 동작

---

## 기술 스택
- Java 17
- Spring Boot 3.5.x
- Spring Web / Validation / Data JPA
- DB: MySQL 8 (앱 실행), H2(in-memory) (test)
- 문서화: springdoc-openapi (Swagger UI)
- 테스트: JUnit5, AssertJ, Spring MockMvc
- 기타: Lombok

---

## 실행 방법

### 1) 사전 준비
- Docker 또는 로컬 MySQL 8
- JDK 17

### 2) MySQL 컨테이너 실행
```bash
docker run --name mysql-todo \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=todo \
  -e MYSQL_USER=todo \
  -e MYSQL_PASSWORD=todoapppassword \
  -p 3306:3306 -d mysql:8.0
```

### 3) 애플리케이션 실행
```bash
./gradlew bootRun
```

### 4) API 문서 확인
Swagger UI: http://localhost:8080/swagger-ui.html

---
## DB 스키마 & 기초 데이터
- 앱 스키마: src/main/resources/schema.sql
- spring.sql.init.mode=always 설정으로 앱 기동 시 자동 실행
- 백업/수동 적용용:
  - db/schema.sql (DDL)
  - db/data.sql (샘플 데이터)
---
## API 명세 (요약)
### 1) ToDo

POST /todos : 할 일 생성
```
Request: {"title": "문자열"}
Response: 201 Created (+ Location 헤더)
```
GET /todos : 목록 조회
```
Response: 200 OK [TodoResponse]
```
PATCH /todos/{publicId} : 부분 업데이트
```
Request: title, completed, priority, deadlineUpdateType(SET|CLEAR), deadline
```
DELETE /todos/{publicId} : 삭제
```
Response: 200 OK (삭제 안내 문자열)
```
TodoResponse 예시
```json
{
    "publicId": "2f3b2e2f-1c7a-4d7c-9b83-1e6f7a7b0c1d",
    "title": "할일 1",
    "completed": false,
    "priority": "NONE",
    "deadline": null
}
```

### 2) 추가 업무 API

POST /todos/reset-completed 완료(true) → 미완료(false) 일괄 리셋
```
Response: 200 OK, body: "N건이 초기화 되었습니다."
```

오류 응답 예시 (ProblemDetail)
```json
{
    "title": "INVALID_INPUT",
    "status": 400,
    "detail": "제목은 공백일 수 없습니다.",
    "code": "INVALID_INPUT",
    "timestamp": "2025-08-23T12:34:56.789"
}
```
---
## 테스트

### 통합 테스트

@SpringBootTest + @ActiveProfiles("test")

Test 프로필 → H2(in-memory), 스케줄러 비활성화

### 슬라이스 테스트

@WebMvcTest(Controller) + MockMvc 요청/응답 검증

### 도메인/리포지토리 테스트

저장/조회, publicId 조회, 제약 위반, 벌크 연산 등

### 실행
```bash
./gradlew test
```
---
## 라이브러리 사용 이유

Spring Data JPA : 반복적인 SQL 작성 부담을 줄이고, 비즈니스 로직에 집중

springdoc-openapi : 컨트롤러 기반 자동 문서화 → Swagger UI 제공

Validation : DTO 수준의 입력 검증 (@NotBlank 등)

Lombok : 보일러플레이트 코드 감소

AOP (LogTraceAspect) : 공통 로깅(호출/예외 트레이싱)

H2 : 테스트 격리 및 속도 향상

---
## 설계/구현 메모

식별자 분리: 내부 PK(id)와 외부 식별자(publicId:UUID) 분리

부분 업데이트 파이프라인: UpdateOperation 전략으로 부분 변경 요청을 안전하게 처리

전역 예외 처리: ProblemDetail 기반 응답 일관화

로깅 AOP: 포인트컷에서 common.log 패키지 제외 → 재귀 방지

배치와 API 일관성: 수동 트리거와 자정 배치가 동일 비즈니스 메서드 사용

DTO 분리 계획: 향후 요구사항 변경 시 Service 전용 DTO 도입 예정