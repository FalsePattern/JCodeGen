/*
 * Copyright (c) 2021 FalsePattern
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.falsepattern.jcodegen;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Builder
@RequiredArgsConstructor
public class CField {
    @Builder.Default
    @NonNull
    public final AccessSpecifier accessSpecifier = AccessSpecifier.builder().build();
    @NonNull
    public final CType type;
    @NonNull
    public final String name;
    @Builder.Default
    @NonNull
    public final String initializer = "";
    @Override
    public String toString() {
        return String.format("%s%s %s%s;", accessSpecifier, type.getSimpleName(), name, initializer.length() > 0 ? " = " + initializer : "");
    }

    public CType type(){return type;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CField)) return false;

        CField cField = (CField) o;

        return name.equals(cField.name);
    }

    public CMethod getter() {
        return CMethod.builder()
                .returnType(type)
                .accessSpecifier(AccessSpecifier.builder().visibility(AccessSpecifier.Visibility.PUBLIC).isStatic(accessSpecifier.isStatic).build())
                .name("get" + name.substring(0, 1).toUpperCase() + name.substring(1))
                .code("return " + name + ";")
                .build();
    }

    public CMethod setter() {
        if (accessSpecifier.isFinal) throw new IllegalArgumentException("Cannot create a setter method for a final variable");
        return CMethod.builder()
                .returnType(CType.VOID)
                .accessSpecifier(AccessSpecifier.builder().visibility(AccessSpecifier.Visibility.PUBLIC).isStatic(accessSpecifier.isStatic).build())
                .name("set" + name.substring(0, 1).toUpperCase() + name.substring(1))
                .paramList(CImmutableList.<CParameter>builder().addParam(CParameter.builder().type(type).name("value").build()).build())
                .code(name + " = value;")
                .build();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
