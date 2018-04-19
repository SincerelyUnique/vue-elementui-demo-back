
package com.example.demo.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

import static com.example.demo.repository.support.ByExampleSpecifications.byExample;
import static com.example.demo.repository.support.ByRangeSpecifications.byRanges;
import static org.springframework.data.jpa.domain.Specifications.where;


/**
 * Shared base repository providing query by example and query by string pattern.
 */
public class CustomRepositoryImpl<E, PK extends Serializable> extends SimpleJpaRepository<E, PK> implements
        CustomRepository<E, PK> {

    private final EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Page<E> findWithExample(E example, List<DateRange<E>> dateRanges, Pageable pageable) {
        Specification<E> byExample = byExample(entityManager, example);
        Specification<E> byRanges = byRanges(dateRanges);
        return findAll(where(byExample).and(byRanges), pageable);
    }

    @Override
    public List<E> findWithExample(E example, List<DateRange<E>> dateRanges) {
        Specification<E> byExample = byExample(entityManager, example);
        Specification<E> byRanges = byRanges(dateRanges);
        return findAll(where(byExample).and(byRanges));
    }









}
