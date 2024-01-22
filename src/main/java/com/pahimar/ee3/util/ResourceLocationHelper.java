package com.pahimar.ee3.util;

import net.minecraft.util.ResourceLocation;

import com.pahimar.ee3.reference.Reference;

public class ResourceLocationHelper {

    public static ResourceLocation getResourceLocation(String modId, String path) {
        return new ResourceLocation(modId, path);
    }

    public static ResourceLocation getResourceLocation(String path) {
        return getResourceLocation(Reference.LOWERCASE_MOD_ID, path);
    }
}
