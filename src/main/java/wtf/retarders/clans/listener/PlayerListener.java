package wtf.retarders.clans.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import wtf.retarders.clans.ClansConstants;
import wtf.retarders.clans.ClansPlugin;
import wtf.retarders.clans.handler.impl.GameHandler;
import wtf.retarders.clans.profile.Profile;
import wtf.retarders.clans.profile.ProfileHandler;
import wtf.retarders.clans.util.item.loadout.impl.LobbyLoadout;

public class PlayerListener implements Listener {

    private ProfileHandler profileHandler = ClansPlugin.getPlugin().getHandlerManager().findHandler(ProfileHandler.class);
    private GameHandler gameHandler = ClansPlugin.getPlugin().getHandlerManager().findHandler(GameHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = new Profile(player.getUniqueId());

        // set player's loadout
        profile.setLoadout(new LobbyLoadout());

        if(Bukkit.getOnlinePlayers().size() == ClansConstants.MIN_PLAYERS_PER_CLAN*4) {
            gameHandler.startGame();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = profileHandler.findProfile(player.getUniqueId());

        if(profile != null) {
            profile.unload();
        }

        if(Bukkit.getOnlinePlayers().size() < ClansConstants.MIN_PLAYERS_PER_CLAN*4) {
            gameHandler.getTask().cancel();
        }
    }
}