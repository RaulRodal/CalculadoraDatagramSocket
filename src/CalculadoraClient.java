import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CalculadoraClient {
	public static void main(String[] args) throws InterruptedException {
		try {
			System.out.println("Creando socket datagrama");

			DatagramSocket datagramSocket = new DatagramSocket();

			System.out.println("Enviando petición al servidor");

			InetAddress serverAddr = InetAddress.getByName("localhost");
			
			//Tipo de operacion
			String mensaje = new String("divide");
			DatagramPacket datagrama = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, serverAddr,5555);
			datagramSocket.send(datagrama);
			
			Thread.sleep(2000);
			//Operando 1
			String operando1 = new String("20");
			DatagramPacket datagrama1 = new DatagramPacket(operando1.getBytes(), operando1.getBytes().length, serverAddr,5555);
			datagramSocket.send(datagrama1);
			
			Thread.sleep(2000);
			//Operando 2
			String operando2 = new String("5");
			DatagramPacket datagrama2 = new DatagramPacket(operando2.getBytes(), operando2.getBytes().length, serverAddr,5555);
			datagramSocket.send(datagrama2);
			
			
			System.out.println("Mensaje enviado");

			System.out.println("Recibiendo respuesta");

			byte[] respuesta = new byte[100];
			DatagramPacket datagramaRecibido = new DatagramPacket(respuesta, respuesta.length);
			datagramSocket.receive(datagramaRecibido);

			System.out.println("Mensaje recibido: " + new String(respuesta).trim());

			System.out.println("Cerrando el socket datagrama");

			datagramSocket.close();

			System.out.println("Terminado");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
