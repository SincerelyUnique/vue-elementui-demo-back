
package com.example.demo.repository.support;

import com.google.common.base.Throwables;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.*;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * Helper to create find by example query.
 */
public class ByExampleSpecifications {
    public static <T> Specification<T> byExample(final EntityManager em, final T example) {
        checkNotNull(em, "em must not be null");
        checkNotNull(example, "example must not be null");
        @SuppressWarnings("unchecked")
        final Class<T> type = (Class<T>) example.getClass();

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                EntityType<T> entity = em.getMetamodel().entity(type);
                for (Attribute<T, ?> attr : entity.getDeclaredAttributes()) {
                    Object attrValue = getValue(example, attr);
                    if (attrValue != null) {
                    	if (attr.getPersistentAttributeType() == MANY_TO_ONE
                                || attr.getPersistentAttributeType() == ONE_TO_ONE) {
                    		final Class<T> type1 = (Class<T>) attrValue.getClass();
                    		EntityType<T> entity1 = em.getMetamodel().entity(type1);
                    		
                    		for (Attribute<T, ?> attr1 : entity1.getDeclaredSingularAttributes()){
                    			Object attrValue1 = getValue((T) attrValue, attr1);
                    			if (attr1.getPersistentAttributeType() == MANY_TO_ONE
                                        || attr1.getPersistentAttributeType() == ONE_TO_ONE){
                    				continue;
                    			}
                    			
                    			if (attr1.getJavaType() == String.class) {
                                    if (isNotEmpty((String) attrValue1)) {
                                    	predicates.add(builder.like((Expression)root.get(attr.getName()).get(attr1.getName()), pattern((String)attrValue1)));
                                    }
                    			}
                    		}
                        }else if(attr.getPersistentAttributeType() == ONE_TO_MANY){
                        	List temp=(List)attrValue;
                        	if(temp.size()>0){
                        	final Class<T> type1 = (Class<T>) temp.get(0).getClass();
                        	EntityType<T> entity1 = em.getMetamodel().entity(type1);
                        	for (Attribute<T, ?> attr1 : entity1.getDeclaredSingularAttributes()){
                        		Object attrValue1 = getValue((T) temp.get(0), attr1);
                        		if (attr1.getJavaType() == String.class) {
                                    if (isNotEmpty((String) attrValue1)) {
                                    	predicates.add(builder.like((Expression)root.join(attr.getName()).<String>get(attr1.getName()), pattern((String)attrValue1)));
//                                    	predicates.add(builder.like((Expression)root.in(attr.getName()).in(attr1.getName()),pattern((String)attrValue1)));
                                    }
                        		}
                        	}
                        	}
                        }else if (attr.getJavaType() == String.class) {
                            if (isNotEmpty((String) attrValue)) {
                                predicates.add(builder.like(root.get(attribute(entity, attr.getName(), String.class)),
                                        pattern((String) attrValue)));
                            }
                        } else {
                            predicates.add(builder.equal(root.get(attribute(entity, attr.getName(), attrValue
                                    .getClass())), attrValue));
                        }
                    }
                    	
                }

                return predicates.isEmpty() ? builder.conjunction() : builder.and(toArray(predicates, Predicate.class));
            }

            private Object getValue(T example, Attribute<T, ?> attr) {
                try {
                    return ReflectionUtils.getField((Field) attr.getJavaMember(), example);
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }

            private <E> SingularAttribute<T, E> attribute(EntityType<T> entity, String fieldName, Class<E> fieldClass) {
                return entity.getDeclaredSingularAttribute(fieldName, fieldClass);
            }
        };
    }

    /**
     * Lookup entities having at least one String attribute matching the passed pattern.
     */
    public static <T> Specification<T> byPatternOnStringAttributes(final EntityManager em, final String pattern,
            final Class<T> type) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                EntityType<T> entity = em.getMetamodel().entity(type);
                for (Attribute<T, ?> attr : entity.getDeclaredSingularAttributes()) {
                    if (attr.getPersistentAttributeType() == MANY_TO_ONE
                            || attr.getPersistentAttributeType() == ONE_TO_ONE) {
                        continue;
                    }
                    if (attr.getJavaType() == String.class && isNotEmpty(pattern)) {
                        predicates.add(builder.like(root.get(attribute(entity, attr)), pattern(pattern)));
                    }
                }
                return predicates.isEmpty() ? builder.conjunction() : builder.or(toArray(predicates, Predicate.class));
            }

            private SingularAttribute<T, String> attribute(EntityType<T> entity, Attribute<T, ?> attr) {
                return entity.getDeclaredSingularAttribute(attr.getName(), String.class);
            }
        };
    }

    static private String pattern(String str) {
        return "%" + str + "%";
    }
}