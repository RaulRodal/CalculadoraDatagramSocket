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
		ret = new String(packet.getData(), 0, packet.getLength()).trim(); // necesario porque no se que longitud tiene
		
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
				DatagramPacket datagrama1 = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(datagrama1);

				String operacion = new String(datagrama1.getData()).trim().toLowerCase();

				InetAddress clientAddr = datagrama1.getAddress();
				int clientPort = datagrama1.getPort();

				System.out.println("operacion recibido: desde " + clientAddr + ", puerto " + clientPort);
				System.out.println("Contenido del operacion: " + operacion);

				if (operacion.equals("suma") || operacion.equals("resta") || operacion.equals("multiplica") || operacion.equals("divide")) {

					System.out.println("Esperando operando 1");
					Integer operando1 = Integer.parseInt(mensajeRecibido(datagramSocket));

					
					System.out.println("Esperando operando 2");
					Integer operando2 = Integer.parseInt(mensajeRecibido(datagramSocket));
					
					
					String mensaje = mensajeOperacion(operacion, operando1, operando2);
					byte[] respuesta = mensaje.getBytes();
					DatagramPacket datagramaRespuesta = new DatagramPacket(respuesta, respuesta.length, clientAddr, clientPort);
					datagramSocket.send(datagramaRespuesta);
					System.out.println("operacion enviado");
				} else {
					System.out.println("operacion recibida no reconocida");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Terminado");
	}
}
