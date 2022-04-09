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

import java.util.*;
import java.util.stream.Collectors;

public class CParamList implements TypeCarrier {
    private final List<CParameter> parameters = new ArrayList<>();

    public CParamList(CParameter... params) {
        Collections.addAll(parameters, params);
    }

    public CParamList(Collection<CParameter> params) {
        parameters.addAll(params);
    }

    public List<CParameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Set<CType> getTypes() {
        return Collections.unmodifiableSet(parameters.stream().map(CParameter::getType).collect(Collectors.toSet()));
    }

    @Override
    public String toString() {
        return parameters.stream().map(CParameter::toString).collect(Collectors.joining(", "));
    }

    public static CParamListBuilder builder() {
        return new CParamListBuilder();
    }

    public static class CParamListBuilder {
        private CParamListBuilder(){}
        private final List<CParameter> params = new ArrayList<>();
        public CParamListBuilder addParam(CParameter param) {
            params.add(param);
            return this;
        }

        public CParamList build() {
            return new CParamList(params);
        }
    }
}
