package clienteUDP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainClienteUDP {

	static Scanner teclado =new Scanner(System.in);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DatagramSocket clientSocket=null;
		try {
			clientSocket=new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InetAddress IPservidor=null;
		try {
			IPservidor= InetAddress.getLocalHost();
			//IPservidor= InetAddress.getByName("192.168.146.250");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int puerto=12345;
		String texto="";
		final DatagramSocket clienteSocket=clientSocket;
		final InetAddress IPservidorFinal=IPservidor;
		final String nick=elegirNick(clienteSocket,IPservidorFinal,puerto);

		Thread enviarMensaje = new Thread(new Runnable() {
            @Override
            public void run() {
            	String texto;
                do {
                	System.out.print("Escribe:");
        			texto=nick+": "+teclado.nextLine();
        			
        			byte[] enviados= new byte[1024];
        			
        			enviados=texto.getBytes();
        			
        			DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidorFinal,puerto);
        			
        			try {
        				clienteSocket.send(envio);
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
				} while (!texto.equalsIgnoreCase("fin"));
            }
        });
		
		enviarMensaje.start();
		
		
		Thread recibirMensaje = new Thread(new Runnable() {
            @Override
            public void run() {
                do {		
		
					byte[] recibidos=new  byte[1024];
					
					DatagramPacket recibo= new DatagramPacket(recibidos,recibidos.length);

					System.out.println("esperando datagrama");
					try {
						clienteSocket.receive(recibo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					byte[] hh=recibo.getData();
					
					for (int i = 0; i < hh.length; i++) {
						if (hh[i]!=0){
							System.out.print((char)hh[i]);
						}
					}
				} while (!texto.equalsIgnoreCase("fin"));
            }
        });
		
		
		recibirMensaje.start();
				
		
	}

	static String elegirNick(DatagramSocket clienteSocket, InetAddress IPservidorFinal,int puerto){
		boolean repetido=true;
		String nick;
		do {
			System.out.print("nick:");
			nick="nombre:"+teclado.nextLine();

			byte[] enviados= new byte[1024];

			enviados=nick.getBytes();

			DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidorFinal,puerto);

			try {
				clienteSocket.send(envio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//enviado ////////////////////////////////////
			byte[] recibidos=new  byte[1024];

			DatagramPacket recibo= new DatagramPacket(recibidos,recibidos.length);

			System.out.println("esperando datagrama");
			try {
				clienteSocket.receive(recibo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] hh=recibo.getData();
			String respuesta="";
			for (int i = 0; i < hh.length; i++) {
				if (hh[i]!=0){
					respuesta+=(char)hh[i];
				}
			}
			System.out.println(respuesta);
			if (respuesta.equalsIgnoreCase("correcto")){
				repetido=false;
			}
		}while (repetido);

		return nick;
	}

}

