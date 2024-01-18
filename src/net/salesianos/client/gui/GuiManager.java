package net.salesianos.client.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.salesianos.client.enums.FrameType;
import net.salesianos.client.socket.SocketManager;
import net.salesianos.shared.constants.GuiParams;
// import net.salesianos.shared.models.Message;
import java.util.Date;


public class GuiManager {
  private JFrame appFrame;
  private String username;
  private SocketManager socketManager;

  public GuiManager() {
    this.socketManager = new SocketManager();
    this.appFrame = new JFrame();
    this.appFrame.setVisible(false);
    this.appFrame.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
    this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void rewriteAppFrame(FrameType frameType) {
    this.appFrame.getContentPane().removeAll();

    switch (frameType) {
      case USERNAME:
        this.appFrame.setTitle("Write your username");
        this.appFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.appFrame.setSize(GuiParams.USERNAME_FRAME_WIDTH, GuiParams.USERNAME_FRAME_HEIGHT);
        break;
      case DATA:
        this.appFrame.setTitle("Send data to server!");
        this.appFrame.setBounds(100, 100, 539, 344);
        // this.appFrame.setLayout(null);
        this.appFrame.setSize(GuiParams.DATA_FRAME_WIDTH, GuiParams.DATA_FRAME_HEIGHT);
        break;
    }
  }

  public void displayFrame(boolean isDisplayed) {
    this.appFrame.setVisible(isDisplayed);
  }

  public void buildUsernameFrame() {
    this.rewriteAppFrame(FrameType.USERNAME);

    JLabel label = new JLabel("Pick a username:");
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);

    JTextField inputBox = new JTextField(GuiParams.USERNAME_INPUT_COLUMNS);

    JButton logInButton = new JButton("Enter Data Server");
    logInButton.addActionListener(e -> this.onClickLogInUsername(inputBox));

    this.appFrame.add(label);
    this.appFrame.add(inputBox);
    this.appFrame.add(logInButton);
  }

  private void onClickLogInUsername(JTextField messageInputBox) {
    String msg = messageInputBox.getText();

    if (msg.length() < 1) {
      System.out.println("No username detected!");
    } else {
      this.username = msg;
      this.socketManager.connectToServer(msg);
      this.displayFrame(false);
      this.buildDataframe();
      this.displayFrame(true);
    }
  }

  private void buildDataframe() {
    this.rewriteAppFrame(FrameType.DATA);

    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(null);

    JLabel nameLabel = new JLabel("msg:");
    nameLabel.setBounds(10, 20, 65, 15);
    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    contentPane.add(nameLabel);

    JTextField msgInputBox = new JTextField();
    msgInputBox.setBounds(81, 20, 191, 19);
    msgInputBox.setColumns(10);
    contentPane.add(msgInputBox);


    JTextArea dataInfoBox = new JTextArea();
    dataInfoBox.setBounds(10, 60, 509, 238);
    contentPane.add(dataInfoBox);
    dataInfoBox.setColumns(60);
    dataInfoBox.setRows(13);
    dataInfoBox.setEditable(false);
    dataInfoBox.setWrapStyleWord(true);
    dataInfoBox.setLineWrap(true);

    JButton messageButton = new JButton("enviar");
    messageButton.setBounds(301, 10, 140, 36);
    messageButton.addActionListener(e -> onClickSendMessageButton(msgInputBox, dataInfoBox));
    contentPane.add(messageButton);

    this.appFrame.setContentPane(contentPane);
    this.socketManager.startServerListener(dataInfoBox);
  }

  private void onClickSendMessageButton(JTextField nameInputBox, JTextArea dataInfoBox) {
    String newMsg = nameInputBox.getText();
    if (newMsg.length() < 1) {
      JOptionPane.showMessageDialog(dataInfoBox, "Algún argumento no se ha escrito.", "Atención", JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      String newWrittenMsg = newMsg;
      DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

      this.socketManager.sendNewMessage(newWrittenMsg);
      dataInfoBox.append("[" + dateFormat.format(date) + "] <" + this.username + ">: " + newWrittenMsg.toString() + "\n");
      nameInputBox.setText("");
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(dataInfoBox, "El numero introducido no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
    }

  }

}
