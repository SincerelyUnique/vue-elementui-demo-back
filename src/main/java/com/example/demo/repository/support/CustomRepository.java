
package com.example.demo.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;


/**
 * Shared base repository providing "query by example" and "query by pattern".
 */
@NoRepositoryBean
public interface CustomRepository<E, PK extends Serializable> extends JpaRepository<E, PK> {

		Page<E> findWithExample(E example, List<DateRange<E>> dateRanges, Pageable pageable);

		//新增-导出
		List<E> findWithExample(E example, List<DateRange<E>> dateRanges);
}
