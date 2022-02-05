2022/02/05
1. 이번 수업에서는 Spring을 사용하지 않고 쌩 자바 + Maven + JPA(Hibernate)를 사용한다.
   1. 가장 중요한 것은 persistance를 사용하는 방법 -> root-resources-META-INF(꼭 여기 해야한다. 만들자)-persistance.xml에 세팅을 해줘야한다. 
   2. 뭐 어떻게 쓸건지는 알아야 할 거 아녀


2. 영속성 컨텍스트의 장점
   1. Entity를 사용할 때 마다 DB에서 찾는 것이 아니라, 1차 캐시(영속 컨텍스트(Entity Manager))안에 있는지 없는지 부터 찾는다.
   2. 근데 솔찌 그렇게까지 큰 이득은 얻지 못한다.(Transaction 내부에서 끝나기 때문)
   3. 1차 캐시로 반복 가능한 읽기 등급의 트랜잭션 격리 수준을 DB가 아니라 APP 차원으로 제공한다.
   4. 쓰기 지연
      1. 엔티티 매니저는 데이터 변경 시 트랜잭션을 시작해야함
      2. persistance를 해도 INSERT문을 DB에 보내지는 않는다.(commit 해야 보냄~!)
   5. 엔티티 수정(변경 감지)(Dirty Checking)
      1. 다시 집어넣어주는 쿼리를 작성할 필요가 없다.
      2. 왜냐하면 엔티티가 변경되었는지 아닌지 확인하는 것이 영속 컨테스트에 있기 때문.
      3. JPA는 Database tranSection이 벌어질 경우, Entity와 SnapShot을 비교함.
         1. 스냅샷이란 가져왔을 때(1차 캐시로) 상태
         2. 엔티티를 변경하면 스냅샷과 다르다는걸 알 수 있다.
      4. 물론~ 영속에서 디테치되면/커밋 안 하면 안바뀐다.
      5. Delete도 마찬가지이다.


3. (뜬금없지만) 영속성 컨텍스트를 플러시하는 방법
   1. em.flush() <- 직접 호출
      1. flush를 한다 해서 1차 캐시가 사라지진 않는다
      2. 그저 쓰기 지연 SQL 저장소에 있는걸 올릴 뿐이다.(뭔가 바뀐거)
      3. 옵션들(em.setFlushMode(FlushModeType.COMMIT))
         1. FlushModeType.AUTO
            1. 커밋이나 쿼리를 실행할 때 플러시(기본값)
         2. FlushModeType.COMMIT
            1. 커밋할 때만 플러시
      4. 영속성 컨텍스트를 비우지 않는다
      5. 변경 내용만 데이터베이스에 동기화한다
      6. 트랜잭션이라는 작업 단위가 중요하다. -> 커밋 직전에만 동기화 하면 된다.
   2. 트랜잭션 커밋 <- 플러시 자동 호출
   3. JPQL 쿼리 실행 <- 플러시 자동 호출
      1. 기본 JPA를 처리하고 있는 상황을 가정해보자
      2. 그런데 flush/commit되기 전에 JPQL 쿼리가 디비로 그냥 날라가게 되면, 두개가 알고 있는 DB 상태는 다를 것이다.
      3. 그래서 JPQL 쿼리가 실행될 때는 무적권 Flush된다.
      

4. 준영속 상태(1차 캐시에 들어가 있는게 영속 상태이다.)
   1. 영속 -> 준영속
   2. 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)
   3. 영속성 컨텍스트가 제공하는 기능을 사용 모단다.
      1. UPDATE(dirty checking) 등등
   4. 만드는 방법
      1. em.detach(entity)
         1. 특정 엔티티만 준영속 상태로 전환
      2. em.clear()
         1. 영속성 컨텍스트를 완전히 초기화
      3. em.close()
         1. 영속성 컨텍스트를 종료


5. 엔티티 매핑
   1. 객체와 테이블 매핑
      1. 소개
         1. 객체와 테이블
            1. @Entity, @Table
            2. @Entity가 붙은 클래스는 JPA가 관리.
            3. @Entity 필수다.
            4. 주의점
               1. 기본 생성자는 필수(파라미터가 없는 public 또는 protected 생성자) 있어야함!
               2. final 클래스, enum, interface, inner 클래스 사용 X
               3. 저장할 필드에 final 사용 X
            5. 속성 정리
               1. name(default == 클래스 이름)
                  1. name="MBR" 로 하면 쿼리에 table 명이 MBR로 나간다.
               2. catalog
               3. schema
               4. uniqueConstraints(DDL)
         2. 필드와 컬럼 매핑
            1. @Column
         3. 기본 키 매핑
            1. @Id
         4. 연관 관계 매핑
            1. @ManyToOne, @JoinColumn
      2. 데이터베이스 스키마 자동 생성
         1. JPA는 DB Table까지 생성하는 기능까지 제공하긴 한다.
            1. 회사에서 상용 장비에 사용하면 넌 쥬거(당연하지?)(DROP을 해서 원래 있던 데이터도 날아간다 이 말이지...)
            2. DDL을 애플리케이션 실행 시점에 자동 생성
            3. 테이블 중심 -> 객체 중심
            4. 데이터베이스 방언에 맞게 적절한 DDL을 생성해준다.
            5. 생성된건 적절히 다듬으면 사용할 수 있긴 하다.
            6. hibernate.hbm2ddl.auto value="속성"를 persistence.xml 추가
               1. 속성
                  1. create : 기존 테이블 삭제 후 다시 생성(DROP -> CREATE)
                  2. create-drop : create와 같지만 종료 시점에 테이블 DROP
                  3. update : 추가 변경분만 반영(운영 DB에는 사용하면 안돼)
                  4. validate : 엔티티와 테이블이 정상 매칭되었는지 확인
                  5. none : 사용하지 않음.
            7. 조심해야할 점
               1. 데이터베이스 방언별로 달라지는 것들(Like VARCHAR(MySQL / Oracle))
               2. 운영 장비에는 create/create-drop/update 사용하면 진짜진짜 뒤진다.(영한이형이 진짜 진지하게 말씀하심)
                  1. 개발 초기 단계는 create 또는 update
                  2. 테스트 서버는 update 또는 validate
                  3. 스테이징과 운영 서버는 validate 또는 none으로 해야한다.
         2. 
   2. 데이터베이스 스키마 자동 생성
   3. 필드와 컬럼 매핑
   4. 기본 키 매핑