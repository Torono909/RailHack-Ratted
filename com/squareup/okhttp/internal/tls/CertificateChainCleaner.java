//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package com.squareup.okhttp.internal.tls;

import java.security.cert.*;
import javax.net.ssl.*;
import java.util.*;
import java.security.*;

public final class CertificateChainCleaner
{
    private static final int MAX_SIGNERS = 9;
    private final TrustRootIndex trustRootIndex;
    
    public CertificateChainCleaner(final TrustRootIndex trustRootIndex) {
        this.trustRootIndex = trustRootIndex;
    }
    
    public List<Certificate> clean(final List<Certificate> chain) throws SSLPeerUnverifiedException {
        final Deque<Certificate> queue = new ArrayDeque<Certificate>(chain);
        final List<Certificate> result = new ArrayList<Certificate>();
        result.add(queue.removeFirst());
        boolean foundTrustedCertificate = false;
    Label_0226:
        for (int c = 0; c < 9; ++c) {
            final X509Certificate toVerify = result.get(result.size() - 1);
            final X509Certificate trustedCert = this.trustRootIndex.findByIssuerAndSignature(toVerify);
            if (trustedCert != null) {
                if (result.size() > 1 || !toVerify.equals(trustedCert)) {
                    result.add(trustedCert);
                }
                if (this.verifySignature(trustedCert, trustedCert)) {
                    return result;
                }
                foundTrustedCertificate = true;
            }
            else {
                final Iterator<Certificate> i = queue.iterator();
                while (i.hasNext()) {
                    final X509Certificate signingCert = i.next();
                    if (this.verifySignature(toVerify, signingCert)) {
                        i.remove();
                        result.add(signingCert);
                        continue Label_0226;
                    }
                }
                if (foundTrustedCertificate) {
                    return result;
                }
                throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + toVerify);
            }
        }
        throw new SSLPeerUnverifiedException("Certificate chain too long: " + result);
    }
    
    private boolean verifySignature(final X509Certificate toVerify, final X509Certificate signingCert) {
        if (!toVerify.getIssuerDN().equals(signingCert.getSubjectDN())) {
            return false;
        }
        try {
            toVerify.verify(signingCert.getPublicKey());
            return true;
        }
        catch (GeneralSecurityException verifyFailed) {
            return false;
        }
    }
}
