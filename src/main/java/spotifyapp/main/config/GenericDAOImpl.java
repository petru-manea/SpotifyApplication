package spotifyapp.main.config;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    private Class<T> persistentClass;

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    private EntityManager entityManager;

    public GenericDAOImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public T findById(ID id) {
        T entity = (T) getEntityManager().find(getPersistentClass(), id);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getEntityManager().createQuery("select x from " + getPersistentClass().getSimpleName() + " x")
                .getResultList();
    }

    @Transactional
    public T save(T entity) {
        getEntityManager().persist(entity);
        return null;
    }

    @Transactional
    public T update(T entity) {
        T mergedEntity = getEntityManager().merge(entity);
        return mergedEntity;
    }

    @Transactional
    public void delete(T entity) {
        if (BaseEntity.class.isAssignableFrom(persistentClass)) {
            ((EntityManager) getEntityManager())
                    .remove(getEntityManager().getReference(entity.getClass(), ((BaseEntity) entity).getId()));
        } else {
            entity = getEntityManager().merge(entity);
            getEntityManager().remove(entity);
        }
    }

    @Transactional
    public void flush() {
        getEntityManager().flush();
    }

}
