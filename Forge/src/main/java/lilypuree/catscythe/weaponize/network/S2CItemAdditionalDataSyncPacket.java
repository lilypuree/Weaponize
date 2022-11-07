package lilypuree.catscythe.weaponize.network;

import lilypuree.catscythe.weaponize.Weaponize;
import lilypuree.catscythe.weaponize.ItemAdditionalData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class S2CItemAdditionalDataSyncPacket {

    private Map<ResourceLocation, ItemAdditionalData> data;

    public S2CItemAdditionalDataSyncPacket(Map<ResourceLocation, ItemAdditionalData> data) {
        this.data = data;
    }

    public S2CItemAdditionalDataSyncPacket(FriendlyByteBuf buf) {
        int n = buf.readInt();
        Map<ResourceLocation, ItemAdditionalData> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            ResourceLocation rl = buf.readResourceLocation();
            ItemAdditionalData element = new ItemAdditionalData(buf);
            map.put(rl, element);
        }
        this.data = map;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        data.forEach((rl, element) -> {
            buf.writeResourceLocation(rl);
            element.toBytes(buf);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Weaponize.DATA_LOADER.getData().clear();
            Weaponize.DATA_LOADER.getData().putAll(data);
        });
        return true;
    }
}
