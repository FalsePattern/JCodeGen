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

import com.falsepattern.jcodegen.util.StringUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashSet;
import java.util.Set;

@Builder
@RequiredArgsConstructor
public class CMethod implements TypeCarrier {
    @Builder.Default
    public final AccessSpecifier accessSpecifier = AccessSpecifier.builder().build();
    @Builder.Default
    public final CType returnType = CType.VOID;
    public final String name;
    @Builder.Default
    public final CParamList paramList = new CParamList();
    public final String code;

    @Override
    public String toString() {
        return String.format("%s%s %s(%s){\n%s}",
                accessSpecifier,
                returnType.getSimpleName(),
                name,
                paramList,
                StringUtil.indent(code, 4));
    }

    @Override
    public Set<CType> getTypes() {
        val result = new HashSet<CType>();
        result.add(returnType);
        result.addAll(paramList.getTypes());
        return result;
    }
}
