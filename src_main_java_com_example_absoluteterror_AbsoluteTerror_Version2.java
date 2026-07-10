package com.example.absoluteterror;

import com.example.absoluteterror.entity.ModEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AbsoluteTerror.MOD_ID)
public class AbsoluteTerror {
    public static final String MOD_ID = "absoluteterror";

    public AbsoluteTerror() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}