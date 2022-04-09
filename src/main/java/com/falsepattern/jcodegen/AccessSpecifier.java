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

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AccessSpecifier {
    public enum Visibility {
        PUBLIC("public"), PACKAGE(""), PRIVATE("private");
        public final String prefix;
        Visibility(String prefix) {
            this.prefix = prefix;
        }
    }
    @Builder.Default
    public final Visibility visibility = Visibility.PRIVATE;
    @Builder.Default
    public final boolean isStatic = false;
    @Builder.Default
    public final boolean isFinal = false;

    @Override
    public String toString() {
        return visibility == Visibility.PACKAGE ? "" : visibility.prefix + " " + (isStatic ? "static " : "") + (isFinal ? "final " : "");
    }
}
