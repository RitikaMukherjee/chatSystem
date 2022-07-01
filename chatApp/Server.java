import java.net.*;

import javax.swing.JFrame;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messegeArea=new JTextArea();
    private JTextField messegeInput=new JTextField();
    private Font font=new Font("Roboto",Font.BOLD,20);
    private Font f=new Font("Roboto",Font.PLAIN,20);
    public Server(){
        try {
            server=new ServerSocket(7777);
            System.out.println("server id ready to accept connection");
            System.out.println("waiting...");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            System.out.println("connection closed");
            System.exit(0);
        }
    }
    private void handleEvents() {
        messegeInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
    
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
    
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend=messegeInput.getText();
                    if(contentToSend.equals("exit")){
                        try {
                            server.close();
                            System.exit(0);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        
                    }
                    messegeArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messegeInput.setText("");
                    messegeInput.requestFocus();
                }
            }
            
        });
    }
    private void createGUI() {
        this.setTitle("Server Messeger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        heading.setFont(font);
        messegeArea.setFont(f);
        messegeInput.setFont(f);
        heading.setIcon(new ImageIcon("chat.png"));
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane scroll=new JScrollPane(messegeArea);
        this.add(scroll,BorderLayout.CENTER);
        this.add(messegeInput,BorderLayout.SOUTH);
        messegeArea.setEditable(false);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,5,20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalAlignment(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.CENTER);
        this.setVisible(true);
    }
    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("reading started");
            try {
                while(true&& !socket.isClosed()){
                    String msg;
                    msg = br.readLine();
                    if(msg.equals("exit")){
                        JOptionPane.showMessageDialog(null, "Server terminated the chat");
                        socket.close();
                        System.exit(0);
                        messegeInput.setEnabled(false);
                        break;
                    }
                    messegeArea.append("CLIENT : "+msg+"\n");
                }
            } catch (IOException e) {
                System.out.println("connection is closed");
                System.exit(0);
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("writer started");
            try {
                while(!socket.isClosed()){
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));

                        String content=br1.readLine();
                        if(content.equals("exit")){
                            socket.close();
                            break;
                        }
                        out.println(content);
                        out.flush();
                }
            }catch (IOException e) {
                System.out.println("connection is closed");
                System.exit(0);
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is server");
        new Server();
    }
}