package com.tonic.woodcutter;

import com.tonic.Static;
import com.tonic.api.widgets.InventoryAPI;
import com.tonic.data.ItemContainerEx;
import com.tonic.data.ItemEx;
import com.tonic.services.ClickManager;
import com.tonic.util.ClickManagerUtil;
import net.runelite.api.gameval.InventoryID;

import java.util.List;

public enum DropStrategy
{
    DROP_FULL,
    DROP_EACH

    ;

    public boolean process()
    {
        switch (this)
        {
            case DROP_FULL:
                if(InventoryAPI.isFull())
                {
                    ItemContainerEx container = new ItemContainerEx(InventoryID.INV);
                    List<ItemEx> items = container.getAll("Logs");
                    for(ItemEx item : items)
                    {
                        ClickManagerUtil.queueClickBox(item);
                        InventoryAPI.interact(item, "Drop");
                    }
                    return true;
                }
                break;
            case DROP_EACH:
                if(InventoryAPI.contains("Logs"))
                {
                    ItemContainerEx container = new ItemContainerEx(InventoryID.INV);
                    List<ItemEx> items = container.getAll("Logs");
                    for(ItemEx item : items)
                    {
                        ClickManagerUtil.queueClickBox(item);
                        InventoryAPI.interact(item, "Drop");
                    }
                    return true;
                }
                break;
        }
        return false;
    }
}
