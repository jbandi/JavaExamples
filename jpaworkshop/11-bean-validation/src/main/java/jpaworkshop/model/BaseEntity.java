package jpaworkshop.model;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    protected Integer id;
    @Version
    protected Integer version;
    protected Timestamp createdAt;
    protected String createdFrom;
    protected Timestamp updatedAt;
    protected String updatedFrom;
}
