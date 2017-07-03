package io.polyglotapps.system;

import lombok.Data;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Data
public class EntityCreatedEvent<T> implements ResolvableTypeProvider {

    private T source;

    public EntityCreatedEvent(final T source) {
        this.source = source;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(source));
    }
}