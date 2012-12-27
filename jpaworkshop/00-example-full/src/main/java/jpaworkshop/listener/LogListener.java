package jpaworkshop.listener;

import java.util.logging.Logger;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import jpaworkshop.model.Employee;

public class LogListener {

    private static Logger logger = Logger.getLogger(LogListener.class.toString());

    @PostLoad
    private void postLoad(Object obj) {
        if (obj instanceof Employee) {
            logger.info("postLoad: " + obj);
        }
    }

    @PostPersist
    private void postPersist(Object obj) {
        logger.info("postPersist: " + obj);
    }

    @PostRemove
    private void postRemove(Object obj) {
        logger.info("postRemove: " + obj);
    }

    @PostUpdate
    private void postUpdate(Object obj) {
        logger.info("postUpdate: " + obj);
    }

    @PrePersist
    private void prePersist(Object obj) {
        logger.info("prePersist: " + obj);
    }

    @PreUpdate
    private void preUpdate(Object obj) {
        logger.info("preUpdate: " + obj);
    }
}
