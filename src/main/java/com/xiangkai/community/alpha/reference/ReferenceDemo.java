package com.xiangkai.community.alpha.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class ReferenceDemo extends PhantomReference<String> {

    public ReferenceDemo(String referent, ReferenceQueue<? super String> q) {
        super(referent, q);
    }

    @Override
    public String get() {
        return super.get();
    }
}
