package it.intesys.codylab.rookie.exception;

public class NotFoundException extends RuntimeException {
    Class<?> entityClass;

    public NotFoundException(Class<?> entityClass, Long id) {
        super (String.format("%s with id %d not found", entityClass.getSimpleName(), id));
        this.entityClass = entityClass;
    }}
