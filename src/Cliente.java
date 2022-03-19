//Hola
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Cliente extends Thread{
	
	public int id;
	public String arch;
	public int total;
	/**
	 * si descarga, no descarga
	 */
	public String descarga;
	
	public Cliente(int id, String arch, String descarga, int total) {
		this.id = id;
		this.arch=arch;
		this.descarga=descarga;
		this.total=total;
	}
 
    public void run() {
 
        //Host del servidor
        final String HOST = "192.168.253.128";
        //Puerto del servidor
        final int PUERTO = 5000;
        DataInputStream in;
        DataOutputStream out;
 
        try {
            //Creo el socket para conectarme con el cliente
            Socket sc = new Socket(HOST, PUERTO);
 
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
 
            //Envio un mensaje al servidor
            out.writeUTF(total+","+arch +","+descarga);
          //Recibo el mensaje del servidor
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            if(descarga.equals("Si")){
            	byte[] contents = new byte[100000];
                //Initialize the FileOutputStream to the output file's full path.
                FileOutputStream fos = new FileOutputStream("data/archivosDescargados/100MB1.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = sc.getInputStream();
                //No of bytes read in one read() call
                int bytesRead = 0;
                while((bytesRead=is.read(contents))!=-1)
                bos.write(contents, 0, bytesRead);
                bos.flush();
     
                out.writeUTF("Se descargó el archivo");

                sc.close();
                System.out.println("Archivo guardado con exito para el cliente: "+id);
            }
            else {
            	System.out.println("Archivo no guardado para el cliente: "+id);
            }
            
 
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
 
}