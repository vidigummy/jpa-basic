2022/02/05
0. 이번 수업에서는 Spring을 사용하지 않고 쌩 자바 + Maven + JPA(Hibernate)를 사용한다.
   1. 가장 중요한 것은 persistance를 사용하는 방법 -> root-resources-META-INF(꼭 여기 해야한다. 만들자)-persistance.xml에 세팅을 해줘야한다. 
   2. 뭐 어떻게 쓸건지는 알아야 할 거 아녀
1. 영속성 컨텍스트의 장점
   1. Entity를 사용할 때 마다 DB에서 찾는 것이 아니라, 1차 캐시(영속 컨텍스트(Entity Manager))안에 있는지 없는지 부터 찾는다.
   2. 근데 솔찌 그렇게까지 큰 이득은 얻지 못한다.(Transaction 내부에서 끝나기 때문)
2. 