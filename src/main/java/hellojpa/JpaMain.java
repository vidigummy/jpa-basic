package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

//            flush!
//            Member member = new Member(200L, "member200");
//            em.persist(member);
//            em.flush();
//            System.out.println("=====flush======");

//            ********준영속 상태********
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAA");
//            em.detach(member);
//            //지금은 뭐 필요 없지만 웹에서 많이 쓴다.



//            ******************* 여기서 부터는 진짜 DB 실습(전의 Entity는 Back에 있다.) *******************
//            1:N 맍들기
//            Create
//            Team team = new Team();
//            team.setName("KAU");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("vidigummy");
//            member.setTeam(team);
//            member.setAge(27);
//            em.persist(member);
//
//            em.flush();
//            em.clear();


//            Member findMember = em.find(Member.class, member.getId());
//
//            List<Member> members = findMember.getTeam().getMembers();
//            for (Member _member : members) {
//                System.out.println("members_  :  " + _member.getName());
//
//            }

//            Team findteam = findMember.getTeam();
//
//            Team newTeam = new Team();
//            newTeam.setName("SW");
//            em.persist(newTeam);
//            findMember.setTeam(newTeam);
//            System.out.println("---------findTeam = "+findteam.getName());


//            상속관계 넣기
//            Movie movie = new Movie();
//            movie.setActor("Vidigummy");
//            movie.setDirector("Dengo");
//            movie.setName("Code In The KAU");
//            movie.setPrice(100);
//            em.persist(movie);
//            em.flush();
//            em.clear();
//
//            Movie findMovie = em.find(Movie.class, movie.getId());
//            System.out.println(findMovie.getDirector());
//            tx.commit();

//            프록시
//            Member member = new Member();
//            member.setName("vi");
//            em.persist(member);
//            em.flush();
//            em.clear();
//
////            Member member1 = em.find(Member.class, member.getId());
//            Member member1 = em.getReference(Member.class, member.getId());

            //JPQL
//            List<Member> result = em.createQuery("select m From Member as m where m.username like '%kim%'", Member.class).getResultList();
            //criteria 사용
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb. createQuery(Member.class);
//            Root<Member> m = query.from(Member.class);
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();

            tx.commit();
        } catch (Exception e){

            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}


