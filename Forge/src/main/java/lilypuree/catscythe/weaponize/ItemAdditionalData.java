package lilypuree.catscythe.weaponize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class ItemAdditionalData {
    public static final Codec<ItemAdditionalData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("attackspeed").forGetter(i -> i.attackSpeed),
            Codec.FLOAT.fieldOf("attackdamage").forGetter(i -> i.attackDamage)
    ).apply(instance, ItemAdditionalData::new));

    private float attackSpeed;
    private float attackDamage;

    public ItemAdditionalData(float attackSpeed, float attackDamage) {
        this.attackSpeed = attackSpeed;
        this.attackDamage = attackDamage;
    }

    public ItemAdditionalData(FriendlyByteBuf buf){
        attackSpeed = buf.readFloat();
        attackDamage = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(attackSpeed);
        buf.writeFloat(attackDamage);
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }
}
