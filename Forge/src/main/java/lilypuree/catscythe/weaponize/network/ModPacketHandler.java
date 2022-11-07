package lilypuree.catscythe.weaponize.network;

import lilypuree.catscythe.weaponize.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel INSTANCE= NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE.messageBuilder(S2CItemAdditionalDataSyncPacket.class, nextID())
                .encoder(S2CItemAdditionalDataSyncPacket::toBytes)
                .decoder(S2CItemAdditionalDataSyncPacket::new)
                .consumer(S2CItemAdditionalDataSyncPacket::handle)
                .add();
    }

}
