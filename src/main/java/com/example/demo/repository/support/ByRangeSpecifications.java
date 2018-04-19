
package com.example.demo.repository.support;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Helper to create {@link Specification} out of {@link DateRange}s.
 */
public class ByRangeSpecifications {

    public static <E,D extends Comparable> Specification<E> byRanges(final List<DateRange<E>> dateRanges) {
        return new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                for (DateRange<E> dateRange : dateRanges) {
                    if (dateRange.isSet()) {
                        Predicate rangePredicate = buildRangePredicate(dateRange, root, builder);

                        if (rangePredicate != null) {
                            if (!dateRange.isIncludeNullSet() || dateRange.getIncludeNull() == FALSE) {
                                predicates.add(rangePredicate);
                            } else {
                                predicates.add(builder.or(rangePredicate, builder.isNull(root.get(dateRange.getField()))));
                            }
                        }

                        // no dateRange at all, let's take the opportunity to keep only null...
                        if (TRUE == dateRange.getIncludeNull()) {
                            predicates.add(builder.isNull(root.get(dateRange.getField())));
                        } else if (FALSE == dateRange.getIncludeNull()) {
                            predicates.add(builder.isNotNull(root.get(dateRange.getField())));
                        }
                    }
                }
                return predicates.isEmpty() ? builder.conjunction() : builder.and(toArray(predicates, Predicate.class));
            }

            private <D extends Comparable<? super D>> Predicate buildRangePredicate(DateRange<E> dateRange, Root<E> root,
                                                                                    CriteriaBuilder builder) {
                if (dateRange.isBetween()) {
                    return builder.between(root.get(dateRange.getField()), dateRange.getFrom(), dateRange.getTo());
                } else if (dateRange.isFromSet()) {
                    return builder.greaterThanOrEqualTo(root.get(dateRange.getField()), dateRange.getFrom());
                } else if (dateRange.isToSet()) {
                    return builder.lessThanOrEqualTo(root.get(dateRange.getField()), dateRange.getTo());
                }
                return null;
            }
        };
    }
}