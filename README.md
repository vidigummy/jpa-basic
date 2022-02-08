2022/02/05
H2는 localhost:8082~
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
         8. DDL 생성 기능
            1. @Column을 적을 수 있다.(name도 바꿀 수 있다.)
            2. unique 값을 true/false | length 제약 조건 할 수 있다. <- DB에 영향을 준다.
            3. 생성할 때만 신경을 써주고 그 이후에는 신경 쓰지 않는다.(runTime에는 영향을 안 주자나)
   3. 필드와 컬럼 매핑
      1. JPA에서 테이블과 엔티티 매핑은 별 거 없서.
         1. 하지만 enum도 있고 자바는 뭐가 많잖아?
      2. 예시
         1. 요구사항
            1. 회원은 일반회원/관리자
            2. 회원 가입일과 수정일
            3. 회원을 설명할 수 있는 필드가 있어야함. 길이제한 X
         2. Member에서 확인
      3. 매핑 어노테이션 정리
         1. @Column
            1. 컬럼 매핑
            2. 속성
               1. name
                  1. 필드와 매핑할 테입르의 컬럼 이름
                  2. 기본: 객체의 필드 이름
               2. insertable/updatable
                  1. 등록, 변경 가능 여부
                  2. 기본: true
               3. nullable(DDL)
                  1. null 값의 허용 여부를 설정, false로 설정하면 DDL 생성 시에 not null 제약조건
               4. unique(DDL)
                  1. @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용한다.
                  2. 잘 안쓴다. 제약조건 더럽게 만든다.(못 알아부요 + 복합 컬럼 안대요)
                  3. 차라리 uniqueConstraints(Entity 단위)를 쓴다~
               5. columnDefinition(DDL)
                  1. 데이터베이스 컬럼 정보를 직접 줄 수 있다. ex) varchar(100) default 'EMPTY'
                  2. 기본: 필드의 자바 타입과 방언 정보 활용
               6. length(DDL)
                  1. 문자 길이 제약조건, String 타입에만 사용한다.
                  2. 기본: 255
               7. precision,scale(DDL)
                  1. BigDecimal 타입에서 사용한다.(BigInteger도 사용할 수 있다.) precision은 소수점을 포함한 전체 자릿수, scale은 소수의 자리수다. 
                  2. 참고로 double/float 타입에는 적용되지 않는다. 정밀한 소수를 다루어야할 때 사용한다.
                  3. 기본 - precision = 19 / scale = 2
         2. @Temporal
            1. 날짜 타입 매핑
            2. LocalDate | LocalDateTime 으로 클래스 설정해주면 생락 가능하다.
            3. 속성
               1. TemporalType.DATE
                  1. 날짜
               2. TemporalType.TIME
                  1. 시간
               3. TemporalType.TIMESTAMP
                  1. 날짜 + 시간
         3. @Enumerated
            1. enum 타입 매핑
            2. 주의사항
               1. EnumType.ORDINAL(ENUM의 순서를 DB에 저장) <- 기본값!
               2. EnumType.STRING(ENUM의 이름을 DB에 저장)
               3. String이 디폴트가 되어야한다...! 무적권 저 속성값을 넣어주도록 하자.
         4. @Lob
            1. BLOB,CLOB 매핑
            2. 지정할 수 있는 속성이 없음
            3. 매핑 필드 타입이 문자면 CLOB / 나머지는 BLOB
         5. @Transient
            1. 디비랑 관련 없이 냅두고 싶은 컬럼 냠냠쨥
   4. 기본 키 매핑
      1. @Id(직접 할당)
         1. String으로 저장이 된다.
         2. 
      2. @GeneratedValue
         1. 속성(strategy = GenerationType.xxx)
            1. IDENTITY: 데이터베이스에 위임, MYSQL
               1. AutoIncrement(MySQL, SQL Server, DB2, PostgreSQL)
               2. JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행
               3. AUTO_INCREMENT는 데이터베이스에 INSERT SQL을 실행 한 이후에 ID값을 알 수 있음
               4. IDENTITY 전략은 em.persist() 시점에 즉시 INSERT SQL 실행하고 DB에서 사용자 조회
                  1. pesrist 하고 나서 그 시점에 바로 DB에 쿼리를 날려뿐다.
                  2. 넣은 다음에 getId 해보면 Id가 나온다.
            2. SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE
               1. @SequenceGenerator 필요(Entity 단위로 씀)
                  1. 주의 : allocationSize 기본값 = 50
                  2. name
                     1. 식별자 생성기 이름(필수)
                  3. sequenceName
                     1. 데이터베이스에 등록되어 있는 시퀀스 이름
                     2. 기본값 : hibernate_sequence
                  4. initialValue
                     1. DDL생성 시에만 사용됨, 시퀀스 DDL을 생성할 때 처음 1 시작하는 수를 지정한다.
                     2. 기본값: 1
                  5. allocationSize
                     1. 시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용됨, 데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값을 반드시 1로 설정해야 한다.)
                     2. 기본값: 50
                     3. 성능을 올리기 위해서 한다.(어떻게?)
                        1. Entity를 저장할 때 마다 네트워크를 타기 때문에 그 성능 저하를 줄이기 위해 쓰는 방법
                        2. 메모리에 n개 일단 지정해 놓고 그 개수 만큼 쓰는 방식
                        3. 그러면 한꺼번에 처리할 수 있다. 패키지마냥.
                  6. catalog,schema
                     1. 데이터베이스 catalog, schema 이름
               2. 특징
                  1. 유일한 값을 순서대로 생성하는 특별한 데이터베이스 오브젝트
                  2. 오라클, PostgreSQL, DB2, H2에서 사용
               3. 전략
                  1. 일단 가져와서 다음 값 commit하는 시점에 다시 집어 넣는다.(버퍼링 가능)
            3. TABLE : 키 생성용 테이블 사용, 모든 DB에서 사용
               1. @TableGenerator 필요
               2. 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
               3. 모든 데이터베이스에서 쓸 수는 있는데... 성능이 딸린다.
               4. 그렇게 많이 쓰지는 않기 때문에 넘어가용(실습 하시긴 함)
            4. AUTO : 방언에 따라 자동 지정, 기본값
6. 연관관계 매핑 기초
   1. 목표
      1. 객체와 테이블 연관관계의 차이
         1. 테이블은 외래키로 조인을 사용해서 연관된 테이블을 찾는다.
         2. 객체는 참조를 사용해서 연관된 객체를 찾는다.
         3. 테이블과 객체 사이에는 이런 간격이 있당
      2. 객체의 참조와 테이블의 외래 키를 매핑
      3. 용어
         1. 방향
         2. 다중성(1:N / N:1 / 1:1 / N:M)
         3. 연관관계의 주인
   2. 단방향 연관관계
      1. 뭐 별거 없당
   3. 양방향 연관관계(와 주인)
      1. 테이블 연관관계는 다르지 않지만 n에서 1은 부를 수 있지만... 1에서 n은 힘들다
      2. mapedby로 쓸 수 있다 이거에용.
         1. 어려워!
         2. 처음에는 이해하기 어렵다.
         3. 테이블은 외래키 하나로 두 테이블의 연관 관계를 관리한다.
         4. 객체는 서로 봐야하잖아?
         5. 외래키 관리를 하는 주체를 정해야 한다.
      3. 연관관계의 주인
         1. 객체 두 관계 중 하나를 연관관계의 주인으로 지정해야한다.
         2. 주인만이 외래키를 관리 할 수 있다.
         3. 주인이 아닌 쪽은 읽기만 가능
         4. 주인은 mappedBy 속성 사용 X
         5. 주인이 아니면 mappedBy 속성으로 주인 지정
         6. 참고사항
            1. 외래키가 있는 곳을 주인으로 정하는게 좋긴 하다.
            2. Team-Member 관계의 경우 Member. Team에는 컬럼에 들어가지 않는 가짜 매핑이 되어있잖아?
            3. 헷갈리지가 않는당
         7. 양방향 매핑을 할 때는 양쪽에 둘 다 값을 넣어주는게 맞다.
            1. 그니까... 그냥 해도 되긴 하는데, 주인이 아닌 list에서 애들 구하려고 하면 SELECT로 DB에 한번 더 가져온다.
            2. 이건 객체지향 방식과 조금 차이가 있지?
            3. em.flush()와 em.clear()을 코드에 집어넣는게 가장 안전하긴 하다.
            4. 근데 그건 웃기니까 양쪽에 항상 값을 넣어주도록 하자.(1차 캐시에)
            5. 추천 방식
               1. 연관관계 편의 메서드
                  1. Members.setTeam(Team team)을(OWNER) 양방향 세팅을 해주는 쪽으로 수정해주거나 추가해준다.
                  2. 바꿀거면 setter/getter은 관례가 있으니까 메서드 이름을 바꿔주는게 좋지 않을까~ ex- changeTeam(Team team)
                  3. 근데 Owner고 follower고 둘 중 하나에만 넣도록 하자.
                  4. 무한 루프도 조심하고 상황에 맞춰서 알잘딱깔센
         8. 정리
            1. 단방향 매핑만으로 이미 연관관계 매핑은 완료
               1. 웬만하면 단방향으로 끝내라...
            2. 양방향 매핑은 반대 방향으로 조회 기능이 추가된 것 뿐
            3. JPQL에서 역방향으로 탐색할 일이 많음
            4. 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨
               1. 테이블에 영향 안 주잖아?
   4. N:M관계
      1. 참
7. 연관관계 매핑
   1. 고려사항
      1. 다중성(앞에 있는게 연관관계 주인이다. 다대일이면 다 가 주인임)
         1. 다대일 : @ManyToOne
            1. 두개의 테이블, 하나의 외래키.(외래키가 있는 곳이 다)
            2. 다대일 단방향
               1. 가장 많이 사용하고, 편한 구조.
            3. 다대일 양방향
               1. 반대쪽에 일대다를 추가한다.
                  1. 테이블에 영향은 안 줘~.
                  2. 반대쪽은 읽기만 가능함.
         2. 일대다 : @OneToMany
            1. 권장하진 않아...
               1. 엔티티가 관리하는 외래 키가 다른 테이블에 있음
               2. 차라리 다대일 양방향으로 가자.
            2. 일대다 단방향
               1. 객체 내부에서 list로 하위 객체들의 정보를 모두 저장하고 아래에서는 상위에 대해 안 가지고 있다.
               2. 하위 객체에서 상위 객체를 알고 싶지 않을 때.(객체 잘못 하면 저렇게 나올 수 있다.)
               3. DB 입장에서는 무적권 다 쪽에 외래키가 들어갈 수 밖에 없다.
               4. JoinColumn을 일 부분에 넣어놓으면 이해는 안되지만 동작하긴 한다.
               5. 이렇게 하면... 정상적으로 해도 INSERT(다) INSERT(일) UPDATE(다) 부분으로 쿼리가 세번 날아간다. 비효율적.
               6. 그리고 테이블 수십갠데, 이런거 쓰면 운영이 힘들어진다.
            3. 일대다 양방향
               1. 그만해. 하지마.
               2. 정리
                  1. 공식적으로는 존재 X
                  2. @JoinColumn(insertable =false, updatable=false)
                  3. 읽기 전용 필드를 사용해서 양방향 처럼 사용하는 방법
                  4. 웩
         3. 일대일 : @OneToOne
            1. 반대도 1:1이다
            2. 주 테이블이나 대상 테이블 중에 외래 키 선택 가능
               1. 주 테이블에 외래 키
               2. 대상 테이블에 외래 키
            3. 외래 키에 데이터베이스 유니크(UNI) 제약 조건 추가
            4. 주 테이블에 외래 키 단방향
               1. 다대일 단방향(UNIQUE 빼고)과 비슷해
            5. 주 테이블에 양방향
               1. 반대편에는 mappedby
            6. 대상 테이블 외래 키 단방향(이건 지원이 안돼요)
            7. 대상 테이블 외래 키 양방향
               1. 되긴 하는데 굳이 그럴거면 왜 하냥?
            8. 정리
               1. 주 테이블에 외래 키
                  1. 주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
                  2. 객체지향 개발자 선호
                  3. JPA 매핑 편리
                  4. 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
                  5. 단점: 값이 없으면 외래 키에 null 허용
               2. 대상 테이블에 외래 키
                  1. 대상 테이블에 외래 키가 존재
                  2. 전통적인 데이터베이스 ㄷ개발자 선호
                  3. 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
                  4. 단점: 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨
         4. 다대다 : @ManyToMany
            1. 실무에서 쓰지 마라! 대체 방법은 나중에 설명해줌
            2. 일단은 두개 가지고 표현을 할 수가 없다.
            3. 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함.
            4. 쓰는 법
               1. @ManyToMany / @JoinTable로 연결 테이블 지정
                  1. 단방향, 양방향 가능
            5. 한계
               1. 편리해 보이지만 실무에서 사용X
               2. 연결 테이블이 단순히 연결만 하고 끝나지 않음
               3. 주만시간, 수량 같은 데이터가 들어올 수 있음.(ORDERAMOUNT/ORDERDATE 등등)
               4. join이 되어서 나와야하는데 중간테이블 때문에 내가 생각치도 못한 테이블이 있음.
            6. 한계 극복
               1. 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
               2. N:1 관계로 풀어낸다.
      2. 단방향, 양방향
         1. 테이블
            1. 외래키 하나로 양쪽 조인 가능함
            2. 사실 방향 개념 X
         2. 객체
            1. 참조용 필드가 있는 쪽으로만 참조 가능
            2. 한쪽만 참조하면 단방향
            3. 양쪽이 서로 참조하면 양방향
            4. 객체는 다대다(두개로)가 된다.(컬렉션 사용)
      3. 연관관계의 주인(양방향)
         1. 테이블은 외래 키 하나로 두 테이블이 연관관계를 맺음
         2. 객체 양방향 관계는 A->B, B->A 처럼 참조가 2군데
         3. 객체 양방향 관계는 참조가 두군데 있음. 둘 중 테이블의 외래키를 관리할 곳을 지정해야함
         4. 연관관계의 주인: 외래 키를 관리하는 참조
         5. 주인의 반대편: 외래 키에 영향을 주지 않음, 단순 조회만 가능.
   2. 컬럼들의 사용법(어노테이션)
      1. @JoinColumn
         1. 외래키를 매핑할 때 사용
         2. name
            1. 매핑할 외래 키 이름
            2. 기본값 : 필드명 + _ + 참조하는 테이블의 기본 키 컬럼명
         3. referencedColumnName
            1. 외래키가 참조하는 대상 테이블의 컬럼명
            2. 기본값 : 참조하는 테이블의 기본키 컬럼명
         4. foreignKey(DDL)
            1. 외래 키 제약을 직접 지정 가능
            2. 테이블을 생성할 때만 사용
         5. unique/nullable insertable / updatable /columnDefinition / table
            1. @Column과 같음
      2. @ManyToOne
         1. optional
            1. false로 설정하면 연관된 엔티티가 항상 있어야한다.
            2. 기본값: true
         2. fetch
            1. 글로벌 페치 전략을 사용한다.
               1. @ManyToOne = EAGER
               2. @OneToMany = LAZY
         3. cascade
            1. 영속성 전이 기능을 사용한다.
         4. targetEntity
            1. 연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용 X
      3. @OneToMany
         1. mappedBy
            1. 연관관계의 주인 필드를 선택한다.
         2. fetch
         3. cascade
         4. targetEntity
8. 상속관계 매핑
   1. 상속관계 매핑
      1. 개념
         1. 관계형 데이터베이스는 상속 객체 X
         2. 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
         3. 상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑
         4. 뭐 어쨌거나 가능은 하다.
      2. 전략
         1. 방법들
            1. 조인전략
               1. 방법
                  1. 공통된 부분을 상위 테이블로 넣고 JOIN한다.(PK,FK 같게)
                  2. 세부 정보는 상위 테이블에 넣는다.
               2. 장점
                  1. 테이블이 정규화가 되어있음. 제약조건도 걸 수 있어.
                  2. 외래키 참조 무결성 제약조건 활용 가능하다.
                  3. 설계가 깔끔해짐
                  4. 저장공간이 효율화된다.(버리는것이 별로 없다.)
               3. 단점
                  1. JOIN을 사용할 수 밖에 없다. 성능 저하
                  2. 조회 쿼리가 복잡
                  3. 데이터 저장시 INSERT 쿼리 2번 나감
                  4. 좀 복잡하지...?
               4. 단점
            2. 단일테이블 전략(기본 전략)
               1. 방법
                  1. 그 논리를 한 테이블로 다 합쳐버린다.(SELECT로 처리할 수 있지~)
               2. 장점
                  1. 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
                  2. 조회 쿼리 단순단순~
               3. 단점
                  1. 자식 엔티티가 매핑하면 null이 들어갈 수 밖에 없다.(무결성...)
                  2. 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있고, 상황에 따라서 조회 성능이 오히려 느려질 수 있다.(임계점까지는 ㄱㅊ)
            3. 구현 클래스마다 테이블 전략
               1. 방법
                  1. 각자 모든 정보를 들고 있는 테이블을 만든다.
               2. 장점
                  1. 없어.
                  2. not null 쓸 수 있긴 하지~
                  3. 서브 타입을 명확하게 구분해서 처리할 떄 효과적
               3. 단점
                  1. 넣을 때는 상관 없지~ 근데 SELECT면 UNION하고 지랄남
                  2. 쓰면 안돼.
                  3. 병신같아. DBA도 싫어해 이건.
                  4. DB 뭐 있는지 다 외울거야?
         2. 주요 어노테이션
            1. @Ingeritance(strategy=InheritanceType.XXX)
               1. JOIND : 조인
                  1. DiscriminatorColumn이 없어도 어떻게든 하긴 한다.
               2. SINGLE_TABLE : 단일 테이블 전략(DEFAULT)
                  1. DiscriminatorColumn 필수
               3. TABLE_PER_CLASS : 구현 클래스마다 테이블 전략
            2. @DiscriminatorColumn(name="DTYPE")
               1. 네임 마음대로 넣을 수 있다~
            3. @DiscriminatorValue("XXX")
               1. 자식에서 쓰는건데, 2의 기준을 살짝 바꿔줄 수 있다(너무 길잖아)
   2. @MappedSuperclass