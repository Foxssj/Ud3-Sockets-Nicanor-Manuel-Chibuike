package net.salesianos.client;

import javax.swing.UIManager;

import net.salesianos.client.gui.GuiManager;

public class ClientGuiApp {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    GuiManager manager = new GuiManager();
    manager.buildUsernameFrame();
    manager.displayFrame(true);
  }

}
