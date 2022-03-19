import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {

	public static void main(String[] args) throws IOException {

		ServerSocket servidor = null;
		Socket sc = null;
		int NumeroServidor=0;
		boolean seguir=true;

		//puerto de nuestro servidor
		final int PUERTO = 5000;

		try {
			//Creamos el socket del servidor
			servidor = new ServerSocket(PUERTO);
			System.out.println("Servidor iniciado");
		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
		//Siempre estara escuchando peticiones
		while (seguir) {

			//Espero a que un cliente se conecte
			sc = servidor.accept();

			NumeroServidor++;
			ServidoresDelegados thread= new ServidoresDelegados(sc, NumeroServidor);

			thread.start();
		}
		//Cierro el socket
		servidor.accept();
		System.out.println("Cliente desconectado");
	}

}