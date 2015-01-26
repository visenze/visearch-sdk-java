package com.visenze.visearch.internal.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.visenze.visearch.*;

public class ViSearchModule extends SimpleModule {

    public ViSearchModule() {
        super("ViSearchModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(ImageResult.class, ImageResultMixin.class);
        context.setMixInAnnotations(Facet.class, FacetMixin.class);
        context.setMixInAnnotations(FacetItem.class, FacetItemMixin.class);
        context.setMixInAnnotations(InsertTransaction.class, InsertTransactionMixin.class);
    }
}
