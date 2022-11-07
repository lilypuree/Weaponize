package lilypuree.catscythe.weaponize;

import commoble.databuddy.data.CodecJsonDataManager;
import lilypuree.catscythe.weaponize.mixin.ItemAccessor;
import lilypuree.catscythe.weaponize.network.ModPacketHandler;
import lilypuree.catscythe.weaponize.network.S2CItemAdditionalDataSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Constants.MOD_ID)
public class Weaponize {

    public static final CodecJsonDataManager<ItemAdditionalData> DATA_LOADER = new CodecJsonDataManager<>("weaponizations", ItemAdditionalData.CODEC);


    public Weaponize() {
        DATA_LOADER.subscribeAsSyncable(ModPacketHandler.INSTANCE, S2CItemAdditionalDataSyncPacket::new);
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(this::onDatapackSync);
        MinecraftForge.EVENT_BUS.addListener(this::onGetAttributeModifier);
    }

    public void setup(FMLCommonSetupEvent event) {
        ModPacketHandler.registerMessages();
    }

    void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(DATA_LOADER);
    }

    void onDatapackSync(OnDatapackSyncEvent event) {
        DATA_LOADER.getData().keySet().forEach(rl -> {
            if (!ForgeRegistries.ITEMS.containsKey(rl)){
                Constants.LOG.warn(rl + " is not a registered item!");
            }
        });
    }

    void onGetAttributeModifier(ItemAttributeModifierEvent event) {

        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
            if (DATA_LOADER.getData().containsKey(key)) {
                ItemAdditionalData data = DATA_LOADER.getData().get(key);
                //subtract the base attribute values
                event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ItemAccessor.getBaseAttackSpeedUUID(), "weaponize_speed", data.getAttackSpeed() - 4.0, AttributeModifier.Operation.ADDITION));
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ItemAccessor.getBaseAttackDamageUUID(), "weaponize_damage", data.getAttackDamage() - 1.0, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}