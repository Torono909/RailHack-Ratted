//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package org.apache.commons.io.filefilter;

import java.io.*;
import java.util.*;

public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable
{
    private static final long serialVersionUID = 5767770777065432721L;
    private final List<IOFileFilter> fileFilters;
    
    public OrFileFilter() {
        this.fileFilters = new ArrayList<IOFileFilter>();
    }
    
    public OrFileFilter(final List<IOFileFilter> fileFilters) {
        if (fileFilters == null) {
            this.fileFilters = new ArrayList<IOFileFilter>();
        }
        else {
            this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
        }
    }
    
    public OrFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        this.fileFilters = new ArrayList<IOFileFilter>(2);
        this.addFileFilter(filter1);
        this.addFileFilter(filter2);
    }
    
    public void addFileFilter(final IOFileFilter ioFileFilter) {
        this.fileFilters.add(ioFileFilter);
    }
    
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList((List<? extends IOFileFilter>)this.fileFilters);
    }
    
    public boolean removeFileFilter(final IOFileFilter ioFileFilter) {
        return this.fileFilters.remove(ioFileFilter);
    }
    
    public void setFileFilters(final List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }
    
    public boolean accept(final File file) {
        for (final IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean accept(final File file, final String name) {
        for (final IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file, name)) {
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.fileFilters != null) {
            for (int i = 0; i < this.fileFilters.size(); ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                final Object filter = this.fileFilters.get(i);
                buffer.append((filter == null) ? "null" : filter.toString());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
