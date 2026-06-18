package it.intesys.codylab.rookie.exception;

import it.intesys.codylab.rookie.domain.Person;

public class IdentityShouldNotBeSetOnCreateException extends RuntimeException {
    Class<?> entityClass;

    public IdentityShouldNotBeSetOnCreateException(Class<?> entityClass) {
        super (entityClass.getSimpleName() + ".id is set on create");

        this.entityClass = entityClass;
    }
}
