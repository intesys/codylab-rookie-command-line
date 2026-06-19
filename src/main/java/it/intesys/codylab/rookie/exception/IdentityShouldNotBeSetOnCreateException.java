package it.intesys.codylab.rookie.exception;

public class IdentityShouldNotBeSetOnCreateException extends ServiceException {
    Class<?> entityClass;

    public IdentityShouldNotBeSetOnCreateException(Class<?> entityClass) {
        super (entityClass.getSimpleName() + ".id is set on create");

        this.entityClass = entityClass;
    }
}
