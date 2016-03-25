package demo.domain;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * An abstract base class that is inherited by other domain classes
 * in the order application context.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
public class BaseEntity implements Serializable {

    private DateTime lastModified;
    private DateTime createdAt;

    public DateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(DateTime lastModified) {
        this.lastModified = lastModified;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "lastModified=" + lastModified +
                ", createdAt=" + createdAt +
                '}';
    }
}
