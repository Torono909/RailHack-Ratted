//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package org.apache.commons.io;

import java.io.*;

public class FileExistsException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public FileExistsException() {
    }
    
    public FileExistsException(final String message) {
        super(message);
    }
    
    public FileExistsException(final File file) {
        super("File " + file + " exists");
    }
}
