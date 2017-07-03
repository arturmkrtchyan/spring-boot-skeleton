package io.polyglotapps.system;

public class ResourceNotFoundException extends RuntimeException {

    private Object resourceId;

    public ResourceNotFoundException(final Object resourceId, final String message) {
        super(message);
        this.resourceId = resourceId;
    }

}
