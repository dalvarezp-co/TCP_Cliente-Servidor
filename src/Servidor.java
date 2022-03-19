import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {
	public static int numeroServidor=0;

	public static void main(String[] args) throws IOException {
		
		Scanner lector = new Scanner(System.in);

		ServerSocket servidor = null;
		
		
		boolean seguir=true;
		//puerto de nuestro servidor
		final int PUERTO = 5000;
		System.out.println("Digite el numero de clientes que desea que se conecten en simultaneo: ");
		int cantClientes = lector.nextInt();

		try {
			//Creamos el socket del servidor
			servidor = new ServerSocket(PUERTO);
			System.out.println("Servidor iniciado");
		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
		Socket[] sc = new Socket[cantClientes];
		numeroServidor=0;
		//Se escuchan las peticiones de los clientes establecidos
		while (numeroServidor<cantClientes) {

			//Espero a que un cliente se conecte
			sc[numeroServidor] = servidor.accept();

			numeroServidor++;
		}
		int listosAEnviar=0;
		while(listosAEnviar<numeroServidor){
			ServidoresDelegados thread= new ServidoresDelegados(sc[listosAEnviar], listosAEnviar);
			thread.start();
			listosAEnviar++;
		}
		//Cierro el socket
		servidor.accept();
		System.out.println("Cliente desconectado");
	}

}