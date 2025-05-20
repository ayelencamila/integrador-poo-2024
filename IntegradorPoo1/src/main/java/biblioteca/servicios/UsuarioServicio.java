package biblioteca.servicios;

import biblioteca.modelo.Miembro;
import biblioteca.modelo.Bibliotecario;
import biblioteca.modelo.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class UsuarioServicio {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");

    public Usuario validarLogin(Long id, String clave) {
        System.out.println("Buscando usuario con ID: " + id);
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario != null) {
            System.out.println("Clave en BD: '" + usuario.getClave() + "'");
            System.out.println("Clave ingresada: '" + clave + "'");
            if (usuario.getClave().equals(clave)) {
                return usuario;
            }
        }
        return null;
    }

    private Usuario buscarUsuarioPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // Primero intenta buscar como Miembro
            TypedQuery<Miembro> queryMiembro = em.createQuery(
                "SELECT m FROM Miembro m WHERE m.id = :id", Miembro.class);
            queryMiembro.setParameter("id", id);
            return queryMiembro.getSingleResult();
        } catch (NoResultException e) {
            // Si no es Miembro, intenta como Bibliotecario
            try {
                TypedQuery<Bibliotecario> queryBiblio = em.createQuery(
                    "SELECT b FROM Bibliotecario b WHERE b.id = :id", Bibliotecario.class);
                queryBiblio.setParameter("id", id);
                return queryBiblio.getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        } finally {
            em.close();
        }
    }
}