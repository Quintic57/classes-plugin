package my.dw.classesplugin.model.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class ActiveAbility implements Ability {

    protected final String name;
    protected final ItemStack itemTrigger;
    protected final int duration;
    protected final int cooldown;
    protected final Map<UUID, Instant> playerCooldowns;

    public ActiveAbility(final String name,
                         final ItemStack itemTrigger,
                         final int duration,
                         final int cooldown,
                         final Map<UUID, Instant> playerCooldowns) {
        this.name = name;
        this.itemTrigger = itemTrigger;
        this.duration = duration;
        this.cooldown = cooldown;
        this.playerCooldowns = playerCooldowns;
    }

    protected static ItemStack generateItemTrigger(final Material material, final String displayName) {
        final ItemStack itemTrigger = new ItemStack(material);
        final ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(displayName);
        itemTrigger.setItemMeta(itemMeta);

        return itemTrigger;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemTrigger() {
        return itemTrigger;
    }

    public long getCooldown() {
        return cooldown;
    }

    public Map<UUID, Instant> getPlayerCooldowns() {
        return playerCooldowns;
    }

    public boolean isAbilityDurationActive(final UUID playerUUID) {
        return this.playerCooldowns.containsKey(playerUUID)
            && Duration.between(this.playerCooldowns.get(playerUUID), Instant.now()).getSeconds() < this.duration;
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return this.playerCooldowns.containsKey(playerUUID)
            && Duration.between(this.playerCooldowns.get(playerUUID), Instant.now()).getSeconds() < this.cooldown;
    }

}
