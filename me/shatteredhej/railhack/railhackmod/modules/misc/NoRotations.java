//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package me.shatteredhej.railhack.railhackmod.modules.misc;

import me.shatteredhej.railhack.railhackmod.module.*;
import me.shatteredhej.railhack.railhackmod.event.events.*;
import me.zero.alpine.fork.listener.*;
import me.shatteredhej.railhack.railhackmod.category.*;
import me.shatteredhej.railhack.railhackmod.event.*;
import net.minecraft.network.play.server.*;
import java.util.function.*;

public class NoRotations extends Module
{
    @EventHandler
    private Listener<EventPacket.SendPacket> listener;
    
    public NoRotations() {
        super(Category.Misc);
        SPacketPlayerPosLook packet;
        this.listener = new Listener<EventPacket.SendPacket>(event -> {
            if (event.get_era() == EventCancellable.Era.EVENT_PRE && event.getPacket() instanceof SPacketPlayerPosLook) {
                packet = (SPacketPlayerPosLook)event.getPacket();
                packet.yaw = NoRotations.mc.player.rotationYaw;
                packet.pitch = NoRotations.mc.player.rotationPitch;
            }
            return;
        }, (Predicate<EventPacket.SendPacket>[])new Predicate[0]);
        this.name = "NoRotate";
        this.tag = "NoRotate";
    }
}
