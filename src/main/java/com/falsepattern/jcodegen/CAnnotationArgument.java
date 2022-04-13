package com.falsepattern.jcodegen;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Objects;

@Builder
@RequiredArgsConstructor
public class CAnnotationArgument {
    private final String name;
    private final String arg;

    public String getName() {return name;}

    public String getArg() {return arg;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        val that = (CAnnotationArgument) obj;
        return Objects.equals(this.arg, that.arg) &&
               Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arg);
    }

    @Override
    public String toString() {
        return name + " = " + arg;
    }

}
