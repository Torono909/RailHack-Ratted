//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package org.apache.commons.io.filefilter;

import java.util.*;

public interface ConditionalFileFilter
{
    void addFileFilter(final IOFileFilter p0);
    
    List<IOFileFilter> getFileFilters();
    
    boolean removeFileFilter(final IOFileFilter p0);
    
    void setFileFilters(final List<IOFileFilter> p0);
}
