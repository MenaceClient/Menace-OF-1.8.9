package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class InventorySyncModule extends BaseModule {

    public short action;

    public InventorySyncModule() {
        super("InventorySync", "Attempts to keep your inventory synced with the server", Category.PLAYER, 0);
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S32PacketConfirmTransaction) {
            final S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction)event.getPacket();
            final Container inventory = mc.thePlayer.inventoryContainer;

            if (packet.getWindowId() == inventory.windowId) {
                this.action = packet.getActionNumber();

                if (this.action > 0 && this.action < inventory.transactionID) {
                    inventory.transactionID = (short) (this.action + 1);
                }
            }
        }
    }

}
