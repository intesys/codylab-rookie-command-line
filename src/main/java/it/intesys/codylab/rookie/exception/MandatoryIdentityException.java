package it.intesys.codylab.rookie.exception;

import it.intesys.codylab.rookie.domain.Person;

public class MandatoryIdentityException extends RuntimeException {
    Class<?> entityClass;

    public MandatoryIdentityException(Class<?> entityClass) {
        super (entityClass.getSimpleName() + ".id should be set on create");
        this.entityClass = entityClass;
    }}
