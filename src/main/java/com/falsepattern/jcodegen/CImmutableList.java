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

import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public class CImmutableList<T> implements TypeCarrier {
    private final List<T> parameters = new ArrayList<>();

    public CImmutableList(Collection<T> params) {
        parameters.addAll(params);
    }

    public List<T> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Set<CType> getTypes() {
        return parameters.stream().filter((x) -> x instanceof TypeCarrier).flatMap((x) -> ((TypeCarrier)x).getTypes().stream()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return parameters.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public static <T> CImmutableListBuilder<T> builder() {
        return new CImmutableListBuilder<>();
    }

    public static class CImmutableListBuilder<T> {
        private CImmutableListBuilder(){}
        private final List<T> params = new ArrayList<>();
        public CImmutableListBuilder<T> addParam(T param) {
            params.add(param);
            return this;
        }

        public CImmutableList<T> build() {
            return new CImmutableList<>(params);
        }
    }
}
