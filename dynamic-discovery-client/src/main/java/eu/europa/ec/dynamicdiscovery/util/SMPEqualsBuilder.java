package eu.europa.ec.dynamicdiscovery.util;

import java.util.List;
import java.util.stream.Collectors;

public class SMPEqualsBuilder {
    private boolean equals = true;

    public <T> SMPEqualsBuilder append(List<T> lhs, List<T> rhs) {

        if (lhs == rhs) {
            return this;
        }
        if (equalsList(lhs, rhs)) {
            return this;
        }
        this.equals = false;
        return this;
    }


    protected <T> boolean equalsList(List<T> lhs, List<T> rhs) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null || lhs.size() != rhs.size()) {
            return false;
        }
        return lhs.stream()
                .filter(val -> !rhs.contains(val))
                .collect(Collectors.toList())
                .isEmpty() &&
                rhs.stream()
                        .filter(val -> !lhs.contains(val))
                        .collect(Collectors.toList())
                        .isEmpty();
    }


    public Boolean build() {
        return Boolean.valueOf(isEquals());
    }

    public boolean isEquals() {
        return equals;
    }

    /**
     * Sets the {@code isEquals} value.
     *
     * @param isEquals The value to set.
     */
    protected void setEquals(final boolean isEquals) {
        this.equals = isEquals;
    }

    /**
     * Reset the Builder to initial value for re-usage.
     */
    public void reset() {
        this.equals = true;
    }
}
