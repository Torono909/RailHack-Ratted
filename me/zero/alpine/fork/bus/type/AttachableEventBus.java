//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package me.zero.alpine.fork.bus.type;

import me.zero.alpine.fork.bus.*;

public interface AttachableEventBus extends EventBus
{
    void attach(final EventBus p0);
    
    void detach(final EventBus p0);
}
