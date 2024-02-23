package servidorUDP;

import datos.Mensaje;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainServerUDP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mensaje mensajes=new Mensaje();
		ArrayList<String> clientes=new ArrayList<>();
		ArrayList<Integer>puertos=new ArrayList<>();
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket(12345);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		
		while (true) {
			
			System.out.println("servidor esperando datagramas");
			DatagramPacket recibo;
			
			byte[] buffer =new byte[1024];
			
			recibo = new DatagramPacket(buffer,buffer.length);
			try {
				socket.receive(recibo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			InetAddress IPorigen=recibo.getAddress();
			int puerto=recibo.getPort();
			boolean existe=false;
			for (int i: puertos){
				if (i==puerto){
					existe=true;
				}
			}
			if (!existe){
				puertos.add(puerto);
			}
						
			String mensaje = new String(recibo.getData()).trim();			
			System.out.println(mensaje);

			byte[] b;
			//enviar mensajes
			DatagramPacket envio;

			boolean repe=false;
			String texto="";
			try {
				texto=mensaje.substring(0,7);
			}catch (StringIndexOutOfBoundsException e){
				e.printStackTrace();
			}
			if (texto.equalsIgnoreCase("nombre:")){
				for (String s: clientes){
					System.out.println(s);
					if (mensaje.equalsIgnoreCase(s)){
						repe=true;
					}
				}
				if (!repe){
					clientes.add(mensaje);
					mensaje="correcto";
					b= mensaje.getBytes();
					envio= new DatagramPacket(b,b.length,IPorigen,puerto);
					try {
						socket.send(envio);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					b= mensaje.getBytes();
					envio= new DatagramPacket(b,b.length,IPorigen,puerto);
					try {
						socket.send(envio);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else if(mensaje.equalsIgnoreCase("mostrar_mensajes")){
				String s=mensajes.mostrar();
				b= s.getBytes();
				envio= new DatagramPacket(b,b.length,IPorigen,puerto);
				try {
					socket.send(envio);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				mensajes.nuevoMensaje(mensaje+"\n");
				b=mensaje.getBytes();
				for (int i : puertos){
					envio= new DatagramPacket(b,b.length,IPorigen,i);
					try {
						socket.send(envio);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			
		}
	}

}
