package top.secundario.gamma.gui;

import java.awt.event.WindowEvent;

@FunctionalInterface
public interface WindowActivatedHandler {
    public void handle(WindowEvent event);
}
