package my.dw.classesplugin.model.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class AssassinCloakAbility extends Ability {

    public AssassinCloakAbility() {
        super( "Cloak", "LEATHER", AbilityType.ACTIVE, 15, 25, new HashMap<>());
    }

    @Override
    public void handleAbility(Player player) {
        if (this.abilityCooldowns.containsKey(player.getUniqueId())
                && Ability.isAbilityOnCooldown(this.abilityCooldowns.get(player.getUniqueId()), 30L)) {
            player.sendMessage(this.name + " is on cooldown. Remaining CD: "
                    + (30L - Duration.between(this.abilityCooldowns.get(player.getUniqueId()), Instant.now()).getSeconds())
                    + " seconds");
            return;
        }

        List<PotionEffect> effects = List.of(
                new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0, false, false),
                new PotionEffect(PotionEffectType.SPEED, 300, 1, false, false));
        player.addPotionEffects(effects);

        this.abilityCooldowns.put(player.getUniqueId(), Instant.now());
    }
}
