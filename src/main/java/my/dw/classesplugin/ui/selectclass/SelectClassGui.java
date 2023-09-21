package my.dw.classesplugin.ui.selectclass;

import my.dw.classesplugin.exception.ClassAlreadyEquippedException;
import my.dw.classesplugin.model.Class;
import my.dw.classesplugin.ui.BaseGui;
import my.dw.classesplugin.ui.GuiFunction;
import my.dw.classesplugin.ui.ItemKey;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SelectClassGui extends BaseGui {

    public static final String NAME = "Select Class";

    public SelectClassGui() {
        super(NAME, (int) Math.ceil(Arrays.stream(Class.values()).filter(Class::isActive).count() / 5.0) * 18);

        // classes displayed in alphabetical order
        final List<Class> activeClasses = Arrays.stream(Class.values())
            .filter(Class::isActive)
            .sorted((Comparator.comparing(Enum::name)))
            .collect(Collectors.toList());
        int inventoryIndex = 0;
        for (int i = 0; i < activeClasses.size(); i++) {
            final Class c = activeClasses.get(i);
            final ItemStack guiItem = getDisplayItem(c);
            final GuiFunction guiFunction = event -> {
                final Player player = (Player) event.getWhoClicked();
                try {
                    c.equipClass(player);
                    player.closeInventory();
                } catch (final ClassAlreadyEquippedException e) {
                    player.sendMessage(e.getMessage());
                }
            };
            setGuiFunction(ItemKey.generate(guiItem), guiFunction);
            getInventory().setItem(inventoryIndex, guiItem);

            if ((i + 1) % 5 == 0) {
                inventoryIndex = inventoryIndex + 10;
            } else {
                inventoryIndex = inventoryIndex + 2;
            }
        }
    }

    private static ItemStack getDisplayItem(final Class clazz) {
        switch (clazz) {
            case ASSASSIN:
                return createDisplayItem(
                    Material.LEATHER,
                    ChatColor.DARK_RED + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses stealth, smoke bombs, and teleports to ambush and fight enemies",
                        "Strengths: Strong single-target damage output, invisibility, high mobility",
                        "Weaknesses: No armor, weak AOE capability, poor range"
                    )
                );
            case ALCHEMIST:
                return createPotionDisplayItem(
                    Material.SPLASH_POTION,
                    Color.ORANGE,
                    ChatColor.GOLD + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses potions to incapacitate and fight enemies",
                        "Strengths: Strong against heavily-armored enemies, good AOE capability",
                        "Weaknesses: Lightly armored, poor range"
                    )
                );
            case ARCHER:
                return createDisplayItem(
                    Material.BOW,
                    ChatColor.LIGHT_PURPLE + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses a bow and speciality arrows to fight enemies",
                        "Strengths: Can toolbox against every kind of enemy, excellent range",
                        "Weaknesses: Lightly armored, weak in close-quarter combat"
                    )
                );
            case JUGGERNAUT:
                return createDisplayItem(
                    Material.NETHERITE_CHESTPLATE,
                    ChatColor.BLUE + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses heavy armor to redirect damage/attention from enemies",
                        "Strengths: Heavily armored, excellent survivability",
                        "Weaknesses: Weak offensive capability, slow movement speed"
                    )
                );
            case SCOUT:
                return createDisplayItem(
                    Material.FEATHER,
                    ChatColor.GREEN + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses mobility skills to quickly traverse the map",
                        "Strengths: Excellent horizontal/vertical mobility, strong team utility",
                        "Weaknesses: Lightly armored, weak offensive capability"
                    )
                );
            case SWORDSMAN:
                return createDisplayItem(
                    Material.IRON_HELMET,
                    ChatColor.GRAY + clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of(
                        "Uses a sword and shield to fight enemies",
                        "Strengths: Strong in close-quarter combat, good survivability",
                        "Weaknesses: Poor range"
                    )
                );
            case CAPTAIN:
            case KNIGHT:
            case SPECIALIST:
            default:
                return createDisplayItem(
                    clazz.getWeapon().getWeaponItemStack().getType(),
                    clazz.name().substring(0, 1) + clazz.name().substring(1).toLowerCase(),
                    List.of("No description implemented")
                );
        }

    }

}
