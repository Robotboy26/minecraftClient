package dev.vili.haiku.event.events;

public class CancellableEvent {
    public interface ICancellable {
        boolean isCancelled();
        void setCancelled(boolean cancelled);
    }

    public interface Cancellable extends ICancellable {
        default void cancel() {
            this.setCancelled(true);
        }

        default void uncancel() {
            this.setCancelled(false);
        }
    }
}
