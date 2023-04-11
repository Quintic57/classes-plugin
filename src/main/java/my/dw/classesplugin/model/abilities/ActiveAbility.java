package my.dw.classesplugin.model.abilities;

import my.dw.classesplugin.model.abilities.assassin.AssassinCloakAbility;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// TODO: Is it possible to add multiple charges for abilities w/ the current framework? Making abilities cancellable?
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

    protected boolean isItemTriggerOnPlayer(final PlayerInventory inventory) {
        return inventory.contains(this.itemTrigger) || inventory.getItemInOffHand().equals(this.itemTrigger);
    }

}
