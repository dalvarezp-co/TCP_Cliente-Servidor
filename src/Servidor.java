import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

	public static void main(String[] args) {

		ServerSocket servidor = null;
		Socket sc = null;
		DataInputStream in;
		DataOutputStream out;

		//puerto de nuestro servidor
		final int PUERTO = 5000;

		try {
			//Creamos el socket del servidor
			servidor = new ServerSocket(PUERTO);
			System.out.println("Servidor iniciado");

			//Siempre estara escuchando peticiones
			while (true) {

				//Espero a que un cliente se conecte
				sc = servidor.accept();

				System.out.println("Cliente conectado");
				in = new DataInputStream(sc.getInputStream());
				out = new DataOutputStream(sc.getOutputStream());

				//Leo el mensaje que me envia
				String mensaje = in.readUTF();

				System.out.println(mensaje);
				//Specify the file
				File file = new File("../100MB.bin");
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				//Get socket's output stream
				OutputStream os = sc.getOutputStream();
				//Read File Contents into contents array
				byte[] contents;
				long fileLength = file.length();
				long current = 0;
				long start = System.nanoTime();
				while(current!=fileLength){
					int size = 100000;
					if(fileLength - current >= size)
						current += size;
					else{
						size = (int)(fileLength - current);
						current = fileLength;
					}
					contents = new byte[size];
					bis.read(contents, 0, size);
					os.write(contents);
					System.out.print("Sending file ... "+(current*100)/fileLength+"% complete!");
				}

				//Le envio un mensaje
				out.writeUTF("Hola mundo desde el servidor!");
				System.out.println("File sent succesfully!");

				//Cierro el socket
				sc.close();
				System.out.println("Cliente desconectado");

			}

		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}