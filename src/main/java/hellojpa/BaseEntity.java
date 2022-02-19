package hellojpa;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    private String createBy;
    private LocalDateTime localDateTime;
    private String lastModifiedBy;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLasModifiedDate() {
        return lasModifiedDate;
    }

    public void setLasModifiedDate(LocalDateTime lasModifiedDate) {
        this.lasModifiedDate = lasModifiedDate;
    }

    private LocalDateTime lasModifiedDate;
}