/*
 * Copyright (c) 2022 Alexander Majka (mfnalex) / JEFF Media GbR
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of  MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jeff_media.morepersistentdatatypes.datatypes;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a generic PersistentDataType. You can provide two functions for converting between the primitive and complex type.
 *
 * @param <T> Primitive Type
 * @param <Z> Complex Type
 */
public class GenericDataType<T, Z> implements PersistentDataType<T, Z> {

    private static final Class<?>[] ALLOWED_TYPES = new Class[]{Byte.class, byte[].class, Double.class, Float.class, Integer.class, int[].class, Long.class, long[].class, Short.class, String.class, PersistentDataContainer.class, PersistentDataContainer[].class};

    private final Class<T> primitiveType;
    private final Class<Z> complexType;
    private final Function<T, Z> toComplex;
    private final Function<Z, T> toPrimitive;


    /**
     * Creates a new generic PersistentDataType.
     *
     * @param primitiveType Primitive type. Must be either byte, byte[], double, float, int, int[], long, long[], short, String, PersistentDataContainer or PersistentDataContainer[]
     * @param complexType   Complex type
     * @param toComplex     Function to convert the primitive to the complex type
     * @param toPrimitive   Function to convert the complex to the primitive type
     */
    public GenericDataType(final Class<T> primitiveType, final Class<Z> complexType, final Function<T, Z> toComplex, final Function<Z, T> toPrimitive) {
        if (!Arrays.stream(ALLOWED_TYPES).anyMatch(clazz -> clazz.equals(primitiveType))) {
            throw new IllegalArgumentException(String.format("Not a valid primitive type: %s. Valid primitive types are: %s", primitiveType.getName(), Arrays.stream(ALLOWED_TYPES).map(Class::getSimpleName).collect(Collectors.joining(", "))));
        }
        this.primitiveType = primitiveType;
        this.complexType = complexType;
        this.toComplex = toComplex;
        this.toPrimitive = toPrimitive;
    }

    @NotNull
    @Override
    public Class<T> getPrimitiveType() {
        return primitiveType;
    }

    @NotNull
    @Override
    public Class<Z> getComplexType() {
        return complexType;
    }

    @NotNull
    @Override
    public T toPrimitive(@NotNull final Z z, @NotNull final PersistentDataAdapterContext persistentDataAdapterContext) {
        return toPrimitive.apply(z);
    }

    @NotNull
    @Override
    public Z fromPrimitive(@NotNull final T t, @NotNull final PersistentDataAdapterContext persistentDataAdapterContext) {
        return toComplex.apply(t);
    }
}
