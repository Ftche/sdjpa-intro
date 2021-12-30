package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Author> findAll() {
        EntityManager em = getEntityManager();
        try{
            TypedQuery<Author> typedQuery = em.createNamedQuery("author_find_all", Author.class);
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        EntityManager em = getEntityManager();

        try{
            Query query = em.createQuery("SELECT a from Author a where a.lastName like :last_name");
            query.setParameter("last_name", lastName);
            List<Author> authors = query.getResultList();
            return authors;
        } finally {
            em.close();
        }

    }

    @Override
    public Author getById(Long id) {
        EntityManager em = getEntityManager();
        Author author = getEntityManager().find(Author.class, id);
        em.close();
        return author;
    }

    @Override
    public Author findAuthorByNameCriteria(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Author> cq = cb.createQuery(Author.class);

            Root<Author> root = cq.from(Author.class);

            ParameterExpression<String> firstNameParam = cb.parameter(String.class);
            ParameterExpression<String> lastNameParam = cb.parameter(String.class);

            Predicate firstNamePred = cb.equal(root.get("firstName"), firstNameParam);
            Predicate lastNamePred = cb.equal(root.get("lastName"), lastNameParam);

            cq.select(root).where(cb.and(firstNamePred, lastNamePred));

            TypedQuery<Author> tq = em.createQuery(cq);
            tq.setParameter(firstNameParam, firstName);
            tq.setParameter(lastNameParam, lastName);

            return tq.getSingleResult();
        } finally {
            em.close();
        }

    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        /*TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
                "WHERE a.firstName = :first_name and a.lastName = :last_name", Author.class); */
        TypedQuery<Author> query = em.createNamedQuery("find_by_name", Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);

        Author author = query.getSingleResult();
        em.close();
        return author;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();

        try {
            em.joinTransaction();
            em.merge(author);
            em.flush();
            em.clear();
            Author saveAuthor = em.find(Author.class, author.getId());
            return saveAuthor;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
















