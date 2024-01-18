package net.salesianos.client.socket.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JTextArea;


public class ServerListener extends Thread {

  private ObjectInputStream msgInStream;
  private JTextArea dataInfoBox;

  public ServerListener(ObjectInputStream socketObjectInputStream, JTextArea dataInfoBox) {
    this.msgInStream = socketObjectInputStream;
    this.dataInfoBox = dataInfoBox;
  }

  @Override
  public void run() {
    try {
      
      while (true) {
        String newServerMessage = (String) this.msgInStream.readObject();
        this.dataInfoBox.append(newServerMessage);
      }
    } catch (IOException | ClassNotFoundException e2) {
      System.out.println("Se ha dejado de escuchar los envíos del servidor.");
    }
  }
}
