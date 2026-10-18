package net.torosamy.torosamyGame.commands;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyGame.utils.ConfigUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;


import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

public class AdminCommands {
    @Command(value = "game reload")
    @Permission("torosamygame.admin")
    public void reloadConfig(CommandSender sender) {
        ConfigUtil.reloadConfig();
        sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.reloadMessage));
    }

    @Command(value = "game test")
    @Permission("torosamygame.admin")
    public void test(Player sender) {
        inspectItemInHand(sender);
    }


    /**
     * 检查玩家手中物品的ItemAdder特殊信息
     * @param player 要检查的玩家
     */
    public static void inspectItemInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你手中没有物品！");
            return;
        }

        player.sendMessage(ChatColor.GOLD + "========== ItemAdder 物品信息 ==========");

        // 基本信息
        player.sendMessage(ChatColor.YELLOW + "物品类型: " + ChatColor.WHITE + item.getType());
        player.sendMessage(ChatColor.YELLOW + "物品名称: " + ChatColor.WHITE +
                (item.getItemMeta() != null && item.getItemMeta().hasDisplayName() ?
                        item.getItemMeta().getDisplayName() : "无显示名称"));
        player.sendMessage(ChatColor.YELLOW + "物品数量: " + ChatColor.WHITE + item.getAmount());

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(ChatColor.RED + "物品没有ItemMeta数据！");
            return;
        }

        // 检查Lore
        if (meta.hasLore()) {
            player.sendMessage(ChatColor.GREEN + "\n物品Lore:");
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                player.sendMessage(ChatColor.GRAY + "  " + (i + 1) + ". " + ChatColor.WHITE + lore.get(i));
            }
        } else {
            player.sendMessage(ChatColor.GRAY + "\n没有Lore信息");
        }

        // 检查PersistentDataContainer (ItemAdder存储自定义数据的地方)
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        player.sendMessage(ChatColor.GREEN + "\nPersistentDataContainer 数据:");

        boolean hasPDCData = false;

        // ItemAdder常用的命名空间键
        inspectPDCKey(pdc, "itemadder", "item_id", player);
        inspectPDCKey(pdc, "itemadder", "namespace", player);
        inspectPDCKey(pdc, "itemadder", "custom_data", player);
        inspectPDCKey(pdc, "itemadder", "durability", player);
        inspectPDCKey(pdc, "itemadder", "food", player);
        inspectPDCKey(pdc, "itemadder", "attributes", player);

        // 也可以检查其他可能的键
        for (NamespacedKey key : pdc.getKeys()) {
            String keyStr = key.toString();
            if (keyStr.contains("itemadder") || keyStr.contains("itemsadder")) {
                hasPDCData = true;
                String value = getPDCValue(pdc, key);
                player.sendMessage(ChatColor.AQUA + "  " + keyStr + ": " + ChatColor.WHITE + value);
            }
        }

        if (!hasPDCData) {
            player.sendMessage(ChatColor.GRAY + "  未找到ItemAdder相关的PDC数据");
        }

        // 检查ItemFlags
        if (!meta.getItemFlags().isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "\n物品标志:");
            meta.getItemFlags().forEach(flag ->
                    player.sendMessage(ChatColor.AQUA + "  " + flag.name()));
        }

        // 检查是否是未拆封的物品（如果适用）
        if (meta.hasCustomModelData()) {
            player.sendMessage(ChatColor.GREEN + "\nCustomModelData: " + ChatColor.WHITE + meta.getCustomModelData());
        }

        // 检查附魔
        if (!meta.getEnchants().isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "\n附魔:");
            meta.getEnchants().forEach((enchant, level) ->
                    player.sendMessage(ChatColor.AQUA + "  " + enchant.getKey().getKey() + " " + level));
        }

        player.sendMessage(ChatColor.GOLD + "========================================");
    }

    /**
     * 检查特定的PDC键
     */
    private static void inspectPDCKey(PersistentDataContainer pdc, String namespace, String key, Player player) {
        NamespacedKey namespacedKey = new NamespacedKey(namespace, key);
        if (pdc.has(namespacedKey)) {
            String value = getPDCValue(pdc, namespacedKey);
            player.sendMessage(ChatColor.AQUA + "  " + namespace + ":" + key + ": " + ChatColor.WHITE + value);
        }
    }

    /**
     * 获取PDC值的字符串表示
     */
    private static String getPDCValue(PersistentDataContainer pdc, NamespacedKey key) {
        // 尝试不同的数据类型
        if (pdc.has(key, PersistentDataType.STRING)) {
            return pdc.get(key, PersistentDataType.STRING);
        } else if (pdc.has(key, PersistentDataType.INTEGER)) {
            return String.valueOf(pdc.get(key, PersistentDataType.INTEGER));
        } else if (pdc.has(key, PersistentDataType.DOUBLE)) {
            return String.valueOf(pdc.get(key, PersistentDataType.DOUBLE));
        } else if (pdc.has(key, PersistentDataType.FLOAT)) {
            return String.valueOf(pdc.get(key, PersistentDataType.FLOAT));
        } else if (pdc.has(key, PersistentDataType.LONG)) {
            return String.valueOf(pdc.get(key, PersistentDataType.LONG));
        } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
            return String.valueOf(pdc.get(key, PersistentDataType.BOOLEAN));
        } else if (pdc.has(key, PersistentDataType.BYTE)) {
            return String.valueOf(pdc.get(key, PersistentDataType.BYTE));
        } else if (pdc.has(key, PersistentDataType.SHORT)) {
            return String.valueOf(pdc.get(key, PersistentDataType.SHORT));
        } else if (pdc.has(key, PersistentDataType.BYTE_ARRAY)) {
            byte[] array = pdc.get(key, PersistentDataType.BYTE_ARRAY);
            return "byte[" + array.length + "]";
        } else if (pdc.has(key, PersistentDataType.INTEGER_ARRAY)) {
            int[] array = pdc.get(key, PersistentDataType.INTEGER_ARRAY);
            return "int[" + array.length + "]";
        } else if (pdc.has(key, PersistentDataType.LONG_ARRAY)) {
            long[] array = pdc.get(key, PersistentDataType.LONG_ARRAY);
            return "long[" + array.length + "]";
        } else {
            return "未知数据类型";
        }
    }

    /**
     * 获取物品的所有原始NBT数据（需要反射或NMS，这里简化处理）
     * 注意：这个方法需要特殊处理，因为Bukkit API不直接支持NBT访问
     */
    public static void inspectRawNBT(Player player) {
        // 注意：这个方法需要依赖其他库如NBT-API或者使用反射
        // 这里只是一个占位，建议使用上面的PDC方法
        player.sendMessage(ChatColor.RED + "原始NBT查看需要额外依赖，建议使用PDC方法");
    }
}
