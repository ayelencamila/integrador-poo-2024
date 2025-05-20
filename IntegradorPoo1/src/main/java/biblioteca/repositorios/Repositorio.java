package biblioteca.repositorios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.Libro;

import java.util.List;

/**
 * Repositorio genérico adaptado al integrador.
 * Compatible con JPA + Hibernate, proyecto Maven y JavaFX.
 * Permite manejar cualquier entidad del modelo de dominio.
 */
public class Repositorio {

    private final EntityManager em;

    public Repositorio(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
    }

    public void iniciarTransaccion() {
        em.getTransaction().begin();
    }

    public void confirmarTransaccion() {
        em.getTransaction().commit();
    }

    public void descartarTransaccion() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void insertar(Object entidad) {
        em.persist(entidad);
    }

    public void modificar(Object entidad) {
        em.merge(entidad);
    }

    public void eliminar(Object entidad) {
        em.remove(em.contains(entidad) ? entidad : em.merge(entidad));
    }

    public void refrescar(Object entidad) {
        em.refresh(entidad);
    }

    public <T> T buscarPorId(Class<T> clase, Object id) {
        return em.find(clase, id);
    }

    public <T> List<T> buscarTodos(Class<T> clase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> consulta = cb.createQuery(clase);
        Root<T> raiz = consulta.from(clase);
        consulta.select(raiz);
        return em.createQuery(consulta).getResultList();
    }

    public <T, P> List<T> buscarTodosOrdenadosPor(Class<T> clase, SingularAttribute<T, P> orden) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> consulta = cb.createQuery(clase);
        Root<T> raiz = consulta.from(clase);
        consulta.select(raiz);
        consulta.orderBy(cb.asc(raiz.get(orden)));
        return em.createQuery(consulta).getResultList();
    }
    public List<CopiaLibro> buscarCopiasDisponiblesConLibros() {
    return em.createQuery(
        "SELECT c FROM CopiaLibro c " +
        "JOIN FETCH c.libro l " +
        "WHERE c.estado = biblioteca.modelo.EstadoCopia.DISPONIBLE " +
        "AND c.esReferencia = false",
        CopiaLibro.class
    ).getResultList();
    }
    
    public List<CopiaLibro> buscarCopiasPorLibro(Libro libro) {
    return em.createQuery(
        "SELECT c FROM CopiaLibro c WHERE c.libro = :libro", CopiaLibro.class)
        .setParameter("libro", libro)
        .getResultList();
}


    public void cerrar() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
