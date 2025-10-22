package com.tonic.woodcutter;

import com.google.inject.Provides;
import com.tonic.Logger;
import com.tonic.Static;
import com.tonic.api.entities.PlayerAPI;
import com.tonic.api.entities.TileObjectAPI;
import com.tonic.data.TileObjectEx;
import com.tonic.plugins.breakhandler.BreakHandler;
import com.tonic.services.ClickManager;
import com.tonic.util.ClickManagerUtil;
import com.tonic.util.VitaPlugin;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import javax.inject.Inject;
import java.awt.image.BufferedImage;

@PluginDescriptor(
        name = "Vita Wood Chopper Pro",
        description = "A sample VitaLite Plugin",
        tags = {"vita", "sample", "woodcutter", "wood", "chopper", "pro"}
)
public class ExamplePlugin extends VitaPlugin
{
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private Client client;

    private final BreakHandler breakHandler = BreakHandler.getInstance();
    private SidePanel panel;
    private NavigationButton navButton;
    private ExamplePluginConfig config;

    @Provides
    ExamplePluginConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ExamplePluginConfig.class);
    }

    @Override
    protected void startUp()
    {
        panel = injector.getInstance(SidePanel.class);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "icon.png");

        navButton = NavigationButton.builder()
                .tooltip("Example Vita Woodcutting Plugin")
                .icon(icon)
                .priority(5)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);

        breakHandler.register(this);
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(navButton);
        panel.shutdown();

        breakHandler.unregister(this);
    }

    @Override
    public void loop()
    {
        if(panel == null || !panel.isRunning())
            return;

        if(breakHandler.isBreaking(this))
            return;

        Player local = client.getLocalPlayer();
        if(!PlayerAPI.isIdle(local))
            return;

        DropStrategy strategy = panel.getSelectedStrategy();
        if(strategy.process())
        {
            return;
        }

        TileObjectEx tree = TileObjectAPI.get("Tree");
        if(tree == null)
        {
            Logger.warn("Could not find a tree!");
            return;
        }

        ClickManagerUtil.queueClickBox(tree);
        TileObjectAPI.interact(tree, "Chop down");
    }
}
