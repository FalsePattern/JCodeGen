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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CType {
    private static final Map<Class<?>, CType> classMap = new HashMap<>();
    private static final Map<String, Map<Integer, CType>> nameArrayMap = new HashMap<>();

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
        if (classMap.containsKey(clazz)) {
            return classMap.get(clazz);
        }
        val name = getBaseTypeOfNDimensionalArray(clazz).getName().replace('$', '.');
        val prim = getBaseTypeOfNDimensionalArray(clazz).isPrimitive();
        val arr = countArrayDimensions(clazz);
        val type = new CType(name, prim, arr);
        classMap.put(clazz, type);
        nameArrayMap.computeIfAbsent(name, (ignored) -> new HashMap<>()).put(arr, type);
        return type;
    }

    public static CType of(String name, int arrayDimensions) {
        if (nameArrayMap.containsKey(name)) {
            val arrayMap = nameArrayMap.get(name);
            if (arrayMap.containsKey(arrayDimensions)) {
                return arrayMap.get(arrayDimensions);
            }
        }
        val type = new CType(name, false, arrayDimensions);
        nameArrayMap.computeIfAbsent(name, (ignored) -> new HashMap<>()).put(arrayDimensions, type);
        return type;
    }

    public static CType of(String name) {
        return of(name, 0);
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
        return CType.of(name, arrayDimensions + 1);
    }

    public CType arrayBaseType() {
        return arrayDimensions == 0 ? this : CType.of(name, 0);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CType)) return false;

        CType cType = (CType) o;

        if (isPrimitive() != cType.isPrimitive()) return false;
        if (arrayDimensions != cType.arrayDimensions) return false;
        return getName().equals(cType.getName());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (isPrimitive() ? 1 : 0);
        result = 31 * result + arrayDimensions;
        return result;
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
