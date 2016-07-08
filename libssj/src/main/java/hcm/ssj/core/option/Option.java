/*
 * Option.java
 * Copyright (c) 2016
 * Authors: Ionut Damian, Michael Dietz, Frank Gaibler, Daniel Langerenken, Simon Flutura
 * *****************************************************
 * This file is part of the Social Signal Interpretation for Java (SSJ) framework
 * developed at the Lab for Human Centered Multimedia of the University of Augsburg.
 *
 * SSJ has been inspired by the SSI (http://openssi.net) framework. SSJ is not a
 * one-to-one port of SSI to Java, it is an approximation. Nor does SSJ pretend
 * to offer SSI's comprehensive functionality and performance (this is java after all).
 * Nevertheless, SSJ borrows a lot of programming patterns from SSI.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package hcm.ssj.core.option;

/**
 * Standard option for SSJ.<br>
 * Created by Frank Gaibler on 04.03.2016.
 */
public class Option<T>
{
    private final String name;
    private T value;
    private final Class<T> type;
    private final String help;

    /**
     * @param name  String
     * @param value T
     * @param type  Class
     * @param help  String
     */
    public Option(String name, T value, Class<T> type, String help)
    {
        this.name = name;
        this.value = value;
        this.type = type;
        this.help = help;
    }

    /**
     * @return String
     */
    public final String getName()
    {
        return name;
    }

    /**
     * @return T
     */
    public final T get()
    {
        return value;
    }

    /**
     * @param value T
     */
    public final void set(T value)
    {
        this.value = value;
    }

    /**
     * @return Class
     */
    public final Class<T> getType()
    {
        return type;
    }

    /**
     * @return String
     */
    public final String getHelp()
    {
        return help;
    }

    /**
     * Tries to set the value by parsing the String parameter. <br>
     * This method will only work with primitives, strings, arrays and enums.
     *
     * @param value String
     * @return boolean
     */
    public final boolean setValue(String value)
    {
        if (!value.isEmpty())
        {
            if (type == Byte.class)
            {
                set((T) Byte.valueOf(value));
                return true;
            } else if (type == Short.class)
            {
                set((T) Short.valueOf(value));
                return true;
            } else if (type == Integer.class)
            {
                set((T) Integer.valueOf(value));
                return true;
            } else if (type == Long.class)
            {
                set((T) Long.valueOf(value));
                return true;
            } else if (type == Float.class)
            {
                set((T) Float.valueOf(value));
                return true;
            } else if (type == Double.class)
            {
                set((T) Double.valueOf(value));
                return true;
            } else if (type == Character.class)
            {
                set((T) (Character) value.charAt(0));
                return true;
            } else if (type == String.class)
            {
                set((T) value);
                return true;
            } else if (type == Boolean.class)
            {
                set((T) Boolean.valueOf(value));
                return true;
            } else if (type.isArray())
            {
                String[] strings = value.replace("[", "").replace("]", "").split(", ");
                Class<?> componentType = type.getComponentType();
                if (componentType.isPrimitive())
                {
                    if (boolean.class.isAssignableFrom(componentType))
                    {
                        boolean[] ar = new boolean[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Boolean.parseBoolean(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (byte.class.isAssignableFrom(componentType))
                    {
                        byte[] ar = new byte[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Byte.parseByte(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (char.class.isAssignableFrom(componentType))
                    {
                        char[] ar = new char[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = strings[i].charAt(0);
                        }
                        set((T) ar);
                        return true;
                    } else if (double.class.isAssignableFrom(componentType))
                    {
                        double[] ar = new double[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Double.parseDouble(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (float.class.isAssignableFrom(componentType))
                    {
                        float[] ar = new float[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Float.parseFloat(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (int.class.isAssignableFrom(componentType))
                    {
                        int[] ar = new int[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Integer.parseInt(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (long.class.isAssignableFrom(componentType))
                    {
                        long[] ar = new long[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Long.parseLong(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    } else if (short.class.isAssignableFrom(componentType))
                    {
                        short[] ar = new short[strings.length];
                        for (int i = 0; i < strings.length; i++)
                        {
                            ar[i] = Short.parseShort(strings[i]);
                        }
                        set((T) ar);
                        return true;
                    }
                } else if (String.class.isAssignableFrom(componentType))
                {
                    set((T) strings);
                    return true;
                }
            } else if (type.isEnum())
            {
                set((T) Enum.valueOf((Class<Enum>) type, value));
                return true;
            }
        } else if (type == String.class)
        {
            set((T) value);
            return true;
        }
        return false;
    }

    /**
     * Verifies if the option is assignable by {@link #setValue(String) setValue}
     *
     * @return boolean
     */
    public boolean isAssignableByString()
    {
        return (type.isEnum()
                || type.isArray()
                || type == Boolean.class
                || type == Character.class
                || type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == String.class);
    }
}