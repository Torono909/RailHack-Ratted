//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package com.sun.jna.win32;

import java.lang.reflect.*;
import com.sun.jna.*;

public class StdCallFunctionMapper implements FunctionMapper
{
    protected int getArgumentNativeStackSize(Class<?> cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = (Class<?>)NativeMappedConverter.getInstance((Class)cls).nativeType();
        }
        if (cls.isArray()) {
            return Pointer.SIZE;
        }
        try {
            return Native.getNativeSize((Class)cls);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown native stack allocation size for " + cls);
        }
    }
    
    public String getFunctionName(final NativeLibrary library, final Method method) {
        String name = method.getName();
        int pop = 0;
        final Class<?>[] parameterTypes;
        final Class<?>[] argTypes = parameterTypes = method.getParameterTypes();
        for (final Class<?> cls : parameterTypes) {
            pop += this.getArgumentNativeStackSize(cls);
        }
        final String decorated = name + "@" + pop;
        final int conv = 63;
        try {
            final Function func = library.getFunction(decorated, conv);
            name = func.getName();
        }
        catch (UnsatisfiedLinkError e) {
            try {
                final Function func2 = library.getFunction("_" + decorated, conv);
                name = func2.getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        return name;
    }
}
