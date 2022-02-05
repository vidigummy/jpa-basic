package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code
        try {
//            CREATE
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("RYU");
//            em.persist(member);

//            READ
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember = " + findMember.getName());

//            UPDATE
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("simba ryu");

//            DELETE
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember = " + findMember.getName());
//            em.remove(findMember);

//            JPQL 기본
//            List<Member> findMembers  = em.createQuery("SELECT m FROM Member as m", Member.class).getResultList();
//            for(Member member : findMembers){
//                System.out.println("member.name   :   "  + member.getName());
//            }

//            응용(paging)
//            List<Member> findMembers  = em.createQuery("SELECT m FROM Member as m WHERE m.id < 3", Member.class)
//                    .setFirstResult(5).setMaxResults(8).getResultList();
//            for(Member member : findMembers){
//                System.out.println("member.name   :   "  + member.getName());
//            }

//            ***********영속 컨텍스트에 대한 이야기***********
//            Member member = new Member();
//            member.setName("vidigummy");
//            member.setId(101L);
////            여기까지는 비영속 상태
//            em.persist(member);
//
////            여기서부터 영속 상태(DB에는 저장이 안 된 상태이다. commit을 해야 DB에 트랜잭션 상태가 변화한다.)
////            em.detach(member);//를 하면 영속성 에서 사라진다.(쿼리도 안 나감~)
//            Member findMember1 = em.find(Member.class, 101L);
//            System.out.println("findMember1 = " + findMember1.getName());
//            em.detach(member);
//            //라고 하면 1차 캐시에서 지워져서 쿼리가 나가지도 않는다. 그리고 find할 때는 1차 캐시에서 찾아서 쿼리를 작성할 필요가 없어진다.!!
//            // 추가적으로, 어떤 entity를 한 트랙잭션 안에서 찾아놓으면, 다시 쿼리를 날리지 않는다. -> 1차 캐시에 남아있기 때문이다~

//            ***********Entity 쓰기 지연에 대한 이야기~~***********
//            CREATE 할 경우
//            Member member1 = new Member(150L, "testA");
//            Member member2 = new Member(151L, "testB");
//
//            em.persist(member1);
//            em.persist(member2);
//            //여기까지는 1차 캐시 및 쓰기 저장소에 들어있을 뿐, DB에 올라가진 않는다.
//            UPDATE의 경우
//            Member member = em.find(Member.class, 150L);
//            member.setName("testU2");
//            em.detach(member);//하면 안바뀌지롱~

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}


