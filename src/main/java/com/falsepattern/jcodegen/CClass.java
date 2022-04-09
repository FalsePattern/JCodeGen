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
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
public class CClass {
    @Getter
    private final String pkg;
    @Builder.Default
    @Getter
    private final AccessSpecifier accessSpecifier = AccessSpecifier.builder().build();
    @Getter
    private final String name;
    @Builder.Default
    @Getter
    private final CType superclass = CType.OBJECT;
    private final Set<CType> imports = new HashSet<>();
    private final List<CField> fields = new ArrayList<>();
    private final List<CConstructor> constructors = new ArrayList<>();
    private final List<CMethod> methods = new ArrayList<>();

    public CType getCType() {
        return CType.of(pkg + "." + name, 0);
    }

    public void importImplicitly(CType type) {
        imports.add(type);
    }

    public void addField(CField field) {
        imports.add(field.type);
        fields.add(field);
    }

    public void addConstructor(CConstructor constructor) {
        imports.addAll(constructor.getTypes());
        constructors.add(constructor);
    }

    public void addMethod(CMethod method) {
        imports.addAll(method.getTypes());
        methods.add(method);
    }

    public void superConstructors(CClass other) {
        constructors.forEach((constructor) -> {
            if (constructor.accessSpecifier.visibility.equals(AccessSpecifier.Visibility.PRIVATE)) return;
            if (constructor.accessSpecifier.visibility.equals(AccessSpecifier.Visibility.PACKAGE) && !other.pkg.equals(pkg)) return;
            other.addConstructor(CConstructor.builder()
                                             .accessSpecifier(AccessSpecifier.builder().visibility(constructor.accessSpecifier.visibility).build())
                                             .paramList(new CParamList(constructor.paramList.getParameters().toArray(new CParameter[0])))
                                             .code("super(" + constructor.paramList.getParameters().stream().map(CParameter::getName).collect(Collectors.joining(", ")) + ");")
                                             .build());
        });
    }

    @Override
    public String toString() {
        var fieldString = fields.stream().map(CField::toString).collect(Collectors.joining("\n"));
        var constructorString = constructors.stream().map(constructor -> constructor.toString(name)).collect(Collectors.joining("\n"));
        val methodString = methods.stream().map(CMethod::toString).collect(Collectors.joining("\n"));
        return String.format(
               "package %s;\n" +
               "\n" +
               "%s\n" +
               "%s\n" +
               "%sclass %s%s {\n" +
               "%s%s" +
               "%s%s" +
               "%s%s" +
               "}",
               pkg,
                imports.stream().filter((cType -> !cType.isPrimitive())).filter(imp -> !imp.getName().equals(pkg + "." + imp.getSimpleName())).map(CType::asImport).sorted().collect(Collectors.joining()),
                superclass != null && !superclass.equals(CType.OBJECT) ? superclass.getName().equals(pkg + "." + superclass.getSimpleName()) ? "" : "import " + superclass.getName() + ";\n" : "",
                accessSpecifier,
                name,
                superclass != null && !superclass.equals(CType.OBJECT) ? " extends " + superclass.getSimpleName() : "",
                StringUtil.indent(fieldString, 4),
                fieldString.length() != 0 && (constructorString.length() != 0 || methodString.length() != 0) ? "\n" : "",
                StringUtil.indent(constructorString, 4),
                constructorString.length() != 0 && methodString.length() != 0 ? "\n" : "",
                StringUtil.indent(methodString, 4),
                methodString.length() != 0 ? "\n" : "");
    }
}
