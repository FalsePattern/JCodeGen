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
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Builder
@RequiredArgsConstructor
public final class CParameter {
    private final CType type;
    private final String name;

    public CType getType() {return type;}

    public String getName() {return name;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        CParameter that = (CParameter) obj;
        return Objects.equals(this.type, that.type) &&
               Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }

    @Override
    public String toString() {
        return type.getSimpleName() + " " + name;
    }

}
