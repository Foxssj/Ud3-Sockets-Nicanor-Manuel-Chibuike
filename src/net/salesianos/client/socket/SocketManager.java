package net.salesianos.client.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

import net.salesianos.client.socket.threads.ServerListener;
import net.salesianos.shared.constants.Ports;
// import net.salesianos.shared.models.Message;

public class SocketManager {

  private Socket socket;
  private ObjectOutputStream objOutStream;
  private ObjectInputStream objInStream;

  public void connectToServer(String username) {
    try {
      this.socket = new Socket("172.16.100.220", Ports.SERVER_PORT);
      this.objOutStream = new ObjectOutputStream(socket.getOutputStream());
      this.objOutStream.writeUTF(username);
    } catch (IOException e) {
      System.out.println("Error al conectar con el servidor: " + e.getMessage());
    }
  }

  public void startServerListener(JTextArea dataInfoBox) {
    try {
      this.objInStream = new ObjectInputStream(socket.getInputStream());
      ServerListener serverListener = new ServerListener(objInStream, dataInfoBox);
      serverListener.start();
    } catch (IOException e) {
      System.out.println("Error escuchando mensajes del servidor: " + e.getMessage());
    }
  }

  public void sendNewMessage(String msg) {
    try {
      objOutStream.writeObject(msg);
    } catch (IOException e) {
      System.out.println("Error enviando mensaje: " + e.getMessage());
    }
  }
}
