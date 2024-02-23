package servidorUDP;

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
		ArrayList<String> clientes=new ArrayList<>();
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket(12345);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			InetAddress IPorigen=recibo.getAddress();
			int puerto=recibo.getPort();		
						
			String mensaje = new String(recibo.getData()).trim();			
			System.out.println(mensaje);
			boolean repe=false;
			if (mensaje.substring(0,7).equalsIgnoreCase("nombre:")){
				for (String s: clientes){
					System.out.println(s);
					if (mensaje.equalsIgnoreCase(s)){
						repe=true;
					}
				}
				if (!repe){
					clientes.add(mensaje);
					mensaje="correcto";
				}
			}

			byte[] b= mensaje.getBytes();
			
			DatagramPacket envio = new DatagramPacket(b,b.length,IPorigen,puerto);
			
			try {
				socket.send(envio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		socket.close();
	}

}
