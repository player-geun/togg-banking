# togg
토스 논술형 문제로 나왔던 개념들을 실전 프로젝트에서 경험해보자.

## 🔍 프로젝트 배경
토스 서술형에 나왔던 개념들을 단순히 이론적으로 학습하기 보다 실전 프로젝트를 통해 직접 구현하며 학습하는 것이 효율적이라고 판단하여 계좌이체 서비스 프로젝트 진행

## 📄 ERD

![image](https://github.com/player-geun/togg/assets/87115015/0cc47b06-58fa-47ec-9f4a-44f6064616f2)

실제 계좌 이체 서비스의 요구사항은 이보다 복잡할 것이지만, 제출 기한 내에 목표한 학습을 진행하기 위해 최소한의 요구사항만 만족한다.

예를 들어, 계좌 잔액의 경우 소수점에 따른 데이터 오차 때문에 고정 소수점을 이용한 DECIMAL 로 표현해야 하지만, 계산의 편의를 위해 INT 로 선언한다.

## 📚 학습한 내용
### 트랜잭션 단위
**"대용량 트래픽을 대비하기 위해 트랜잭션 분리"**

계좌 개설은 실시간 성이 요구되는 서비스는 아니라고 판단하여, 대용량 트래픽에 대한 최적화 지점이라고 생각했다.

읽기 연산은 쓰기 연산 보다 비용이 낮기 때문에, 두 연산의 실행 속도가 다르다. 회원 조회와 계좌 저장을 분리하여, 향후 메세지 큐와 함께 조회 로직이 끝난 후 저장 메세지를 produce 하여 비동기적으로 처리 가능하다.

또한, DB 와 커넥션을 짧게 가져가기 때문에 충돌도 줄일 수 있다.

### Lock
**“데이터의 정합성을 보장하기 위해 배타 락을 통한 비관적 락을 적용”**

낙관적 락의 경우, 대부분 DB에 락을 거는 것이 아니기 때문에 애플리케이션 상의 복구 로직에 문제가 생기는 경우 데이터 정합성이 깨질 수도 있다고 생각했다.

계좌 이체의 경우, 데이터 정합성이 매우 중요한 서비스라고 판단하여 DB에 배타 락을 적용한 비관적 락을 선택했다. 단, 배타락의 경우 성능을 많이 떨어뜨리기 때문에, 성능과 안전성에 대한 트레이드 오프를 많이 고민해봐야 할 것 같다.

### Fetch Join
**“계좌이체 내역 조회의 경우 FETCH JOIN 을 통해 성능 최적화”**

현재 계좌 aggregate 에서 Account 가 Root Entity 가 되어, 하나의 Repository 가 다른 Entity 인 AccountTransfer 도 관리한다.

지연 로딩으로 설정했기 때문에, Account 안의 멤버 변수인 List<AccountTransfer> 는 N + 1 문제가 발생한다. FETCH JOIN 을 통해 처음부터 연관된 테이블을 함께 가져와서 성능을 최적화한다.

## 📌 더 학습하면 좋을 점

1. 분리된 트랜잭션 상황에서 실패에 대한 복구 로직은 어떻게 구성할지
2. 페이지네이션에 따른 최적화 문제 고민

