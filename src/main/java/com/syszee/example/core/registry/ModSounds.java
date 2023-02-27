package com.syszee.example.core.registry;

import com.syszee.example.core.ModMain;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent EXAMPLE = register("example_sound");

    public static void init() {
    }

    private static SoundEvent register(String id) {
        ResourceLocation location = new ResourceLocation(ModMain.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, location, new SoundEvent(location));
    }
}