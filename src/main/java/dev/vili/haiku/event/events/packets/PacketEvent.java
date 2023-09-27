
package dev.vili.haiku.event.events.packets;

import net.minecraft.network.packet.Packet;

public class PacketEvent {
    public static class Receive {
        private static final Receive INSTANCE = new Receive();

        private boolean cancelled = false;
        public Packet<?> packet;

        public static Receive get(Packet<?> packet) {
            INSTANCE.cancelled = false;
            INSTANCE.packet = packet;
            return INSTANCE;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }

    public static class Send {
        private static final Send INSTANCE = new Send();

        private boolean cancelled = false;
        public Packet<?> packet;

        public static Send get(Packet<?> packet) {
            INSTANCE.cancelled = false;
            INSTANCE.packet = packet;
            return INSTANCE;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public Packet<?> getPacket()
		{
			return packet;
		}

        public void setPacket(Packet<?> newPacket) {
            this.packet = newPacket;
        }
    }

    public static class Sent {
        private static final Sent INSTANCE = new Sent();

        public Packet<?> packet;

        public static Sent get(Packet<?> packet) {
            INSTANCE.packet = packet;
            return INSTANCE;
        }
    }
}