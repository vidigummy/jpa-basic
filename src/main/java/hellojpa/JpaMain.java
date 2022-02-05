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

//            영속 컨텍스트에 대한 이야기
            Member member = new Member();
            member.setName("vidigummy");
            member.setId(10L);
//            여기까지는 비영속 상태
            em.persist(member);

//            여기서부터 영속 상태(DB에는 저장이 안 된 상태이다. commit을 해야 DB에 트랜잭션 상태가 변화한다.)
//            em.detach(member);//를 하면 영속성 에서 사라진다.(쿼리도 안 나감~)



            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}


