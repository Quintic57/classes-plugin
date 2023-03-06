package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class Ability {

    String name;
    String itemTrigger;
    AbilityType type;
    int duration;
    int cooldown;
    Map<UUID, Instant> abilityCooldowns;

    public Ability(String name,
                   String itemTrigger,
                   AbilityType type,
                   int duration,
                   int cooldown,
                   Map<UUID, Instant> abilityCooldowns) {
        this.name = name;
        this.itemTrigger = itemTrigger;
        this.type = type;
        this.duration = duration;
        this.cooldown = cooldown;
        this.abilityCooldowns = abilityCooldowns;
    }

    public String getItemTrigger() {
        return itemTrigger;
    }

    public AbilityType getType() {
        return type;
    }

    public abstract void handleAbility(Player player);

    public static boolean isAbilityOnCooldown(final Instant lastUsedTime, final Long cooldown) {
        return Duration.between(lastUsedTime, Instant.now()).getSeconds() < cooldown;
    }

}
