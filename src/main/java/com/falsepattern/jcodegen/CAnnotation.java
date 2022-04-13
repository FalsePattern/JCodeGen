package com.falsepattern.jcodegen;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

@Builder
public class CAnnotation implements TypeCarrier{
    @NonNull
    @Getter
    public final CType type;
    @Builder.Default
    @Getter
    public final CImmutableList<CAnnotationArgument> params = new CImmutableList<>(Collections.emptySet());

    @Override
    public Set<CType> getTypes() {
        return Collections.singleton(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CAnnotation)) return false;

        CAnnotation that = (CAnnotation) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return "@" + type.getSimpleName() + (params.getTypes().size() > 0 ? "(" + params + ")" : "");
    }
}
