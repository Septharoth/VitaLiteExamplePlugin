package com.tonic.woodcutter;

import com.tonic.Logger;
import com.tonic.Static;
import com.tonic.api.entities.PlayerAPI;
import com.tonic.api.entities.TileObjectAPI;
import com.tonic.data.TileObjectEx;
import com.tonic.services.ClickManager;
import com.tonic.util.VitaPlugin;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
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

    private SidePanel panel;
    private NavigationButton navButton;

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
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(navButton);
        panel.shutdown();
    }

    @Override
    public void loop()
    {
        if(panel == null || !panel.isRunning())
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

        ClickManager.queueClickBox(getClickBox(tree));
        TileObjectAPI.interact(tree, "Chop down");
    }

    private Rectangle getClickBox(TileObjectEx object)
    {
        Shape shape = object.getTileObject().getClickbox();
        if(shape != null)
        {
            return shape.getBounds();
        }
        return Static.getRuneLite().getGameApplet().getWorldViewportArea();
    }
}
