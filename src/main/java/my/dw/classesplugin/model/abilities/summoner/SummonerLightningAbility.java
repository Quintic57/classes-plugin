package my.dw.classesplugin.model.abilities.summoner;

import static my.dw.classesplugin.utils.AbilityUtils.generateItemMetaTrigger;

import my.dw.classesplugin.model.abilities.ActiveAbility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SummonerLightningAbility extends ActiveAbility {

    public SummonerLightningAbility() {
        super(
            "Lightning Staff",
            generateItemMetaTrigger(Material.BLAZE_ROD, "Lightning Staff"),
            0,
            10
        );
    }

    @Override
    public boolean handleAbility(final Player player) {
        final Block targetBlock = player.getTargetBlock(null, 50);
        if (targetBlock.getType().equals(Material.AIR)) {
            player.sendMessage("The target block is too far away");
            return false;
        }

        targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
        return true;
    }
}
