//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package com.squareup.okhttp.internal.tls;

import java.security.cert.*;

public interface TrustRootIndex
{
    X509Certificate findByIssuerAndSignature(final X509Certificate p0);
}
