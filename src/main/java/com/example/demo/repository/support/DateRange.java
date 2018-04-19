
package com.example.demo.repository.support;

import java.io.Serializable;
import java.util.Date;

/**
 * DateRange support for {@link Comparable} types.
 */
public class DateRange<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String field;
    private Date from;
    private Date to;
    private Boolean includeNull;

    /**
     * Constructs a new {@link DateRange} with no boundaries and no restrictions on field's nullability.
     * @param field the attribute of an existing entity.
     */
    public DateRange(String field) {
        this.field = field;
    }

    /**
     * Constructs a new DateRange.
     *
     * @param field the property's name of an existing entity.
     * @param from the lower boundary of this range. Null means no lower boundary.
     * @param to the upper boundary of this range. Null means no upper boundary.
     */
    public DateRange(String field, Date from, Date to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    /**
     * Constructs a new DateRange.
     *
     * @param field an entity's attribute
     * @param from the lower boundary of this range. Null means no lower boundary.
     * @param to the upper boundary of this range. Null means no upper boundary.
     * @param includeNull tells whether null should be filtered out or not.
     */
    public DateRange(String field, Date from, Date to, Boolean includeNull) {
        this.field = field;
        this.from = from;
        this.to = to;
        this.includeNull = includeNull;
    }

    /**
     * Constructs a new DateRange by copy.
     */
    public DateRange(DateRange<E> other) {
        this.field = other.getField();
        this.from = other.getFrom();
        this.to = other.getTo();
        this.includeNull = other.getIncludeNull();
    }

    /**
     * @return the entity's attribute this {@link DateRange} refers to.
     */
    public String getField() {
        return field;
    }

    /**
     * @return the lower range boundary or null for unbound lower range.
     */
    public Date getFrom() {
        return from;
    }

    /**
     * Sets the lower range boundary. Accepts null for unbound lower range.
     */
    public void setFrom(Date from) {
        this.from = from;
    }

    public boolean isFromSet() {
        return getFrom() != null;
    }

    /**
     * @return the upper range boundary or null for unbound upper range.
     */
    public Date getTo() {
        return to;
    }

    /**
     * Sets the upper range boundary. Accepts null for unbound upper range.
     */
    public void setTo(Date to) {
        this.to = to;
    }

    public boolean isToSet() {
        return getTo() != null;
    }

    public void setIncludeNull(boolean includeNull) {
        this.includeNull = includeNull;
    }

    public Boolean getIncludeNull() {
        return includeNull;
    }

    public boolean isIncludeNullSet() {
        return includeNull != null;
    }

    public boolean isBetween() {
        return isFromSet() && isToSet();
    }

    public boolean isSet() {
        return isFromSet() || isToSet() || isIncludeNullSet();
    }

    public boolean isValid() {
        if (isBetween()) {
            return getFrom().compareTo(getTo()) <= 0;
        }

        return true;
    }
}