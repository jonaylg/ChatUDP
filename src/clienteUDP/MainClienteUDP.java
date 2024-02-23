package clienteUDP;

import datos.Cuadro;

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
		Cuadro c=new Cuadro();
		c.setVisible(true);
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
		mensajesAnteriores(clienteSocket,IPservidorFinal,puerto,c);
		Thread enviarMensaje = new Thread(new Runnable() {
            @Override
            public void run() {
				System.out.print("Escribe:");
            	String texto;
                do {
        			texto=c.esperarEnter();
        			
        			byte[] enviados= new byte[1024];
        			
        			if (!texto.equalsIgnoreCase("")&&texto!=null){
						texto=nick+": "+texto;
						enviados=texto.getBytes();

						DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidorFinal,puerto);

						try {
							clienteSocket.send(envio);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

					try {
						clienteSocket.receive(recibo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					byte[] hh=recibo.getData();
					String mensajeReci="";
					for (int i = 0; i < hh.length; i++) {
						if (hh[i]!=0){
							mensajeReci+=(char)hh[i];
						}
					}
					c.recibirMensaje(mensajeReci);
					System.out.println(mensajeReci);
					System.out.println();
				} while (!texto.equalsIgnoreCase("fin"));
            }
        });
		
		
		recibirMensaje.start();
				
		
	}

	static String elegirNick(DatagramSocket clienteSocket, InetAddress IPservidorFinal,int puerto){
		boolean repetido=true;
		String nick;
		String nombre;
		do {
			System.out.print("nick:");
			nick=teclado.nextLine();
			nombre="nombre:"+nick;

			byte[] enviados= new byte[1024];

			enviados=nombre.getBytes();

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
			if (respuesta.equalsIgnoreCase("correcto")){
				repetido=false;
			}
		}while (repetido);

		return nick;
	}

	static void mensajesAnteriores(DatagramSocket clienteSocket, InetAddress IPservidorFinal,int puerto, Cuadro c){
		System.out.print("quieres ver los mensajes anteriores en el chat?");
		String respuesta=teclado.nextLine();

		byte[] enviados= new byte[1024];

		if (respuesta.equalsIgnoreCase("si")){
			enviados="mostrar_mensajes".getBytes();

			DatagramPacket envio = new DatagramPacket(enviados,enviados.length,IPservidorFinal,puerto);

			try {
				clienteSocket.send(envio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			String mensajes="";
			for (int i = 0; i < hh.length; i++) {
				if (hh[i]!=0){
					mensajes+=(char)hh[i];
				}
			}
			c.recibirMensaje(mensajes);
		}

	}

}

