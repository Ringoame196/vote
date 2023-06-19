package com.github.ringoame196.vote.Commands;

import com.sun.org.apache.xpath.internal.axes.SubContextList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.chat.*;

public class vote implements CommandExecutor , TabCompleter {
    String vote_status = "false";
    List<String> vote_list;
    List<String> vote_type;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length==0){return true;}

        String subcommand = "/.create/.result";
        if(subcommand.contains("/"+strings[0])) {
            if (!commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.RED + "権限が必要です");
                return true;
            }

            String sub_command = strings[0];
            if (sub_command.equals(".create")) {//作成
                if (vote_status.equals("true")) {
                    commandSender.sendMessage(ChatColor.RED + "既に投票が開始されています");
                    return true;
                }

                if (strings.length <= 2) {
                    commandSender.sendMessage(ChatColor.RED + "情報が不足しています");
                    return true;
                }
                vote_type = new ArrayList<>();
                vote_list = new ArrayList<>();
                for (int i = 1; i < strings.length; i++) {
                    if (i == 10) {
                        commandSender.sendMessage(ChatColor.RED + "選択肢が多すぎたので減らして設定しました");
                    }
                    if (i < 10) {
                        vote_list.add(strings[i]);
                    }
                }
                Bukkit.broadcastMessage("-----------------");
                Bukkit.broadcastMessage(ChatColor.AQUA + "[投票開始] " + strings[1]);
                {
                    BaseComponent[] message = TextComponent.fromLegacyText(ChatColor.GOLD + "投票する場合は /vote または " + ChatColor.YELLOW + "[ここをクリック]");
                    ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/vote ");
                    HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("投票").create());
                    message[1].setClickEvent(clickEvent);
                    message[1].setHoverEvent(hoverEvent);
                    Bukkit.getServer().spigot().broadcast(message);
                }

                vote_status = "true";

            } else if (sub_command.equals(".result")) {//結果
                if (vote_status.equals("false")) {
                    commandSender.sendMessage(ChatColor.RED + "投票が開始されていません");
                    return true;
                }
                Bukkit.broadcastMessage(ChatColor.AQUA + "---" + vote_list.get(0) + "の投票結果---");
                {
                    if (vote_type.size() == 0) {
                        Bukkit.broadcastMessage(ChatColor.RED + "投票されませんでした(´；ω；｀)");
                    } else {
                        for (int i = 0; i < vote_type.size(); i++) {
                            Bukkit.broadcastMessage(ChatColor.AQUA+"[投票"+(i+1)+"] "+vote_type.get(i));
                        }
                    }
                    vote_status = "false";
                }
            }
        }
        else
        {//投票
            if (!(commandSender instanceof Player)) {
            // プレイヤーでない場合の処理
                commandSender.sendMessage(ChatColor.RED+"プレイヤーのみ投票可能です");
                return true;
            }
            Player player = (Player) commandSender;
            if(!vote_list.contains(strings[0]))
            {
                player.sendMessage(ChatColor.RED+"選択肢以外は投票できません");
                return true;
            }
            vote_type.add(strings[0]);
            Bukkit.broadcastMessage(ChatColor.YELLOW+"<"+player.getDisplayName()+"> 投票完了");
            player.sendMessage(ChatColor.YELLOW+strings[0]+"に投票しました");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();

        // プレイヤー以外からのコマンド実行を防止する
        if (!(commandSender instanceof Player)) {
            return completions;
        }

        Player player = (Player) commandSender;

        // サブコマンドのタブ補完
        if (strings.length == 1) {
            String partialCommand = strings[0].toLowerCase();
            // サブコマンドの候補を追加する
            completions.add(".create");
            completions.add(".result");

            if(vote_list!=null) {
                for (int i = 1; i < vote_list.size(); i++) {
                    completions.add(vote_list.get(i));
                }
            }
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase(".create")) {
            // create サブコマンドの第二引数の補完
            completions.add("タイトル");
        } else if (strings.length >= 3 && strings[0].equalsIgnoreCase(".create")) {
            // create サブコマンドの選択肢の補完
            int choiceIndex = strings.length - 2;
            completions.add("選択肢" + choiceIndex);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
        }

        return completions;
    }
}
