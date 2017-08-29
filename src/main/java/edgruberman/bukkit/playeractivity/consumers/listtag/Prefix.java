package edgruberman.bukkit.playeractivity.consumers.listtag;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static edgruberman.bukkit.playeractivity.Main.chat;

class Prefix {

	private Player player;
	private String prefix;
	
	Prefix(Player player) {
		this.player = player;
		retrievePrefix();
		formatPrefix();
	}
	
	String getPrefix() {
		return prefix;
	}
	
	private void retrievePrefix() {
		prefix = chat.getPlayerPrefix(player);
	}
	
	private void formatPrefix() {
		switch (prefix) {
			case "&4":
				prefix = ChatColor.DARK_RED + "";
				break;
			case "&c":
				prefix = ChatColor.RED + "";
				break;
			case "&e":
				prefix = ChatColor.YELLOW + "";
				break;
			case "&6":
				prefix = ChatColor.GOLD + "";
				break;
			case "&2":
				prefix = ChatColor.DARK_GREEN + "";
				break;
			case "&a":
				prefix = ChatColor.GREEN + "";
				break;
			case "&b":
				prefix = ChatColor.AQUA + "";
				break;
			case "&3":
				prefix = ChatColor.DARK_AQUA + "";
				break;
			case "&1":
				prefix = ChatColor.DARK_BLUE + "";
				break;
			case "&9":
				prefix = ChatColor.BLUE + "";
				break;
			case "&d":
				prefix = ChatColor.LIGHT_PURPLE + "";
				break; 
			case "&5":
				prefix = ChatColor.DARK_PURPLE + "";
				break;
			case "&f":
				prefix = ChatColor.WHITE + "";
				break;
			case "&7":
				prefix = ChatColor.GRAY + "";
				break;
			case "&8":
				prefix = ChatColor.DARK_GRAY + "";
				break;
			case "&0":
				prefix = ChatColor.BLACK + "";
				break;
			default:
				break;	
		}
	}
}
