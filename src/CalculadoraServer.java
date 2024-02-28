import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Date;

public class CalculadoraServer {
	
	public static String mensajeRecibido(DatagramSocket datagramSocket) {
		String ret;
		byte[] buffer = new byte[100];
		// Crea un paquete para recibir datos
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        // Recibe el paquete
		try {
			datagramSocket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Se quitan los espacios porque no se sabe que longitud tiene
		ret = new String(packet.getData(), 0, packet.getLength()).trim(); 
		
		return ret;
	}
	
	public static String mensajeOperacion(String operacion, int operando1, int operando2) {
		String ret = "";
		
		switch (operacion) {
		case "suma":
			ret = "Resultado de la suma: " + operando1 + "+" + operando2 + "=" + (operando1+operando2);
			break;
		case "resta":
			ret = "Resultado de la resta: " + operando1 + "-" + operando2 + "=" + (operando1-operando2);
			break;
		case "multiplica":
			ret = "Resultado de la multiplicacion: " + operando1 + "x" + operando2 + "=" + (operando1*operando2);
			break;
		case "divide":
			ret = "Resultado de la division: " + operando1 + "/" + operando2 + "=" + (operando1/operando2);
			break;
		default:
			ret = "Operacion no entendida";
			break;
		}
		return ret;
	}
	
	public static void main(String[] args) {
		System.out.println("Arrancando servidor calculadora.");
		DatagramSocket datagramSocket = null;
		try {
			InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
			datagramSocket = new DatagramSocket(addr);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while (datagramSocket != null) {
			try {
				System.out.println("Esperando operacion");

				byte[] buffer = new byte[100];
				DatagramPacket datagrama = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(datagrama);

				String operacion = new String(datagrama.getData()).trim().toLowerCase();

				InetAddress clientAddr = datagrama.getAddress();
				int clientPort = datagrama.getPort();

				System.out.println("Operacion recibida: desde " + clientAddr + ", puerto " + clientPort);
				System.out.println("Tipo de operacion: " + operacion);

				if (operacion.equals("suma") || operacion.equals("resta") || operacion.equals("multiplica") || operacion.equals("divide")) {

					System.out.println("Esperando operando 1");
					Integer operando1 = Integer.parseInt(mensajeRecibido(datagramSocket));

					
					System.out.println("Esperando operando 2");
					Integer operando2 = Integer.parseInt(mensajeRecibido(datagramSocket));
					
					// Creacion del mensaje
					String mensaje = mensajeOperacion(operacion, operando1, operando2);
					byte[] respuesta = mensaje.getBytes();
					DatagramPacket datagramaRespuesta = new DatagramPacket(respuesta, respuesta.length, clientAddr, clientPort);
					// Envio del mensaje
					datagramSocket.send(datagramaRespuesta);
					System.out.println("Resultado enviado");
				} else {
					System.out.println("Operacion recibida no reconocida");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Terminado");
	}
}
