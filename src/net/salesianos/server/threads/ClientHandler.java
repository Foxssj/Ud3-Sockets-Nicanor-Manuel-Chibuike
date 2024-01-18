package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class ClientHandler extends Thread {

  private ObjectInputStream clientMsgInStream;
  private ObjectOutputStream clientObjOutStream;
  private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;

  public ClientHandler(ObjectInputStream clientMsgInStream, ObjectOutputStream clientObjOutStream,
      ArrayList<ObjectOutputStream> connectedObjOutputStreamList) {
    this.clientMsgInStream = clientMsgInStream;
    this.clientObjOutStream = clientObjOutStream;
    this.connectedObjOutputStreamList = connectedObjOutputStreamList;
  }

  @Override
  public void run() {
    String username = "";
    boolean exit = true;
    
    try {
      username = this.clientMsgInStream.readUTF(); 


      while (exit) {
        String messageRecieved = (String) this.clientMsgInStream.readObject();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        
        messageRecieved = "[" + dateFormat.format(date) + "] <" + username + ">: " + messageRecieved + "\n";
        System.out.println(messageRecieved);
        
        if (messageRecieved.contains("bye")) {
          exit = false;
          this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
          System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
        }
        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
          if (otherObjOutputStream != this.clientObjOutStream) {
            otherObjOutputStream.writeObject(messageRecieved);
          }
          
        }
        
        

        if (username == "") {
          System.out.println("c rompio");
          exit = false;
        }

      }

    } catch (EOFException eofException) {
      this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
      eofException.printStackTrace();
      System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
