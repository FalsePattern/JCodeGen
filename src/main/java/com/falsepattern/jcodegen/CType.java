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
import lombok.val;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CType {
    private static final Map<Class<?>, CType> typeMap = new HashMap<>();

    public static final CType VOID = CType.of(void.class);
    public static final CType BYTE = CType.of(byte.class);
    public static final CType CHAR = CType.of(char.class);
    public static final CType SHORT = CType.of(short.class);
    public static final CType INT = CType.of(int.class);
    public static final CType LONG = CType.of(long.class);
    public static final CType FLOAT = CType.of(float.class);
    public static final CType DOUBLE = CType.of(double.class);
    public static final CType OBJECT = CType.of(Object.class);

    public static CType of(Class<?> clazz) {
        if (typeMap.containsKey(clazz)) {
            return typeMap.get(clazz);
        }
        val cType = new CType(
                getBaseTypeOfNDimensionalArray(clazz).getName().replace('$', '.'),
                getBaseTypeOfNDimensionalArray(clazz).isPrimitive(),
                countArrayDimensions(clazz));
        typeMap.put(clazz, cType);
        return cType;
    }

    private final String name;
    private final boolean primitive;
    private final int arrayDimensions;

    private CType(String name, boolean primitive, int arrayDimensions) {
        if (arrayDimensions < 0 || arrayDimensions > 255) throw new IllegalArgumentException("Array dimensions must be between 0 and 255 (inclusive)");
        this.name = name;
        this.primitive = primitive;
        this.arrayDimensions = arrayDimensions;
    }

    public CType(String name, int arrayDimensions) {
        this(name, false, arrayDimensions);
    }

    public CType(String name) {
        this(name, 0);
    }

    public String getName() {
        val r = new StringBuilder();
        r.append(getNameAsImport());
        for (int i = 0; i < arrayDimensions; i++) {
            r.append("[]");
        }
        return r.toString();
    }

    public String getNameAsImport() {
        return name;
    }

    public String getSimpleName() {
        val split = name.split("\\.");
        val r = new StringBuilder();
        r.append(split[split.length - 1]);
        for (int i = 0; i < arrayDimensions; i++) {
            r.append("[]");
        }
        return r.toString();
    }

    public boolean isArray() {
        return arrayDimensions > 0;
    }

    public int arrayDimensions() {
        return arrayDimensions;
    }

    public CType arrayOf() {
        return new CType(name, arrayDimensions + 1);
    }

    public CType arrayBaseType() {
        return arrayDimensions == 0 ? this : new CType(name, 0);
    }

    public boolean isPrimitive() {return primitive;}

    public Optional<Class<?>> tryGetClass(ClassLoader classLoader) {
        Class<?> baseType;
        if (primitive) {
            switch (name) {
                case "void": baseType = void.class; break;
                case "byte": baseType = byte.class; break;
                case "char": baseType = char.class; break;
                case "short": baseType = short.class; break;
                case "int": baseType = int.class; break;
                case "long": baseType = long.class; break;
                case "float": baseType = float.class; break;
                case "double": baseType = double.class; break;
                default: baseType = null; break;
            }
        } else {
            try {
                baseType = Class.forName(name, false, classLoader);
            } catch (ClassNotFoundException e) {
                baseType = null;
            }
        }
        if (baseType == null)
            return Optional.empty();
        else {
            if (arrayDimensions == 0) {
                return Optional.of(baseType);
            } else {
                int[] sizes = new int[arrayDimensions];
                return Optional.ofNullable(Array.newInstance(baseType, sizes).getClass());
            }
        }

    }

    public String asImport() {
        return primitive ? "" : String.format("import %s;\n", name);
    }

    @SuppressWarnings("ConstantConditions") //Small conflict with lombok, can be safely ignored
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        CType that = (CType) obj;
        return Objects.equals(this.name, that.name) &&
               this.primitive == that.primitive &&
               this.arrayDimensions == that.arrayDimensions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, primitive, arrayDimensions);
    }

    private static Class<?> getBaseTypeOfNDimensionalArray(Class<?> clazz) {
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    private static int countArrayDimensions(Class<?> clazz) {
        int dimensions = 0;
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            dimensions++;
        }
        return dimensions;
    }
}
