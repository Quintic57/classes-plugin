package my.dw.classesplugin.model.abilities;

import my.dw.classesplugin.ClassesPlugin;
import my.dw.classesplugin.utils.AbilityUtils;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class ArrowAbility implements Ability {

    protected final String name;
    protected final ItemStack arrowTrigger;
    protected final int cooldown;
    protected final Map<UUID, Instant> playerCooldowns;

    public final static String ARROW_METADATA_KEY = "arrow_ability_name";

    public ArrowAbility(final String name,
                        final ItemStack arrowTrigger,
                        final int cooldown,
                        final Map<UUID, Instant> playerCooldowns) {
        this.name = name;
        this.arrowTrigger = arrowTrigger;
        this.cooldown = cooldown;
        this.playerCooldowns = playerCooldowns;
    }

    public String getName() {
        return name;
    }

    public ItemStack getArrowTrigger() {
        return arrowTrigger;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Map<UUID, Instant> getPlayerCooldowns() {
        return playerCooldowns;
    }

    public boolean isAbilityOnCooldown(final UUID playerUUID) {
        return this.playerCooldowns.containsKey(playerUUID)
                && Duration.between(this.playerCooldowns.get(playerUUID), Instant.now()).getSeconds() < this.cooldown;
    }

    @Override
    public boolean handleAbility(final Player player) {
        AbilityUtils.removeItemFromPlayer(player.getInventory(), this.arrowTrigger);
        player.getInventory().addItem(this.arrowTrigger);

        return true;
    }

    public void onProjectileLaunch(final AbstractArrow arrow) {
        arrow.setMetadata(ArrowAbility.ARROW_METADATA_KEY,
            new FixedMetadataValue(ClassesPlugin.getPlugin(), this.name));
    }

    public void onProjectileHit(final ProjectileHitEvent event) {
        event.getEntity().remove();
    }

}

