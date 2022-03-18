//Hola
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Cliente {
 
    public static void main(String[] args) {
 
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
 
            //Envio un mensaje al cliente
            out.writeUTF("¡Hola mundo desde el cliente!");
            
            byte[] contents = new byte[100000];
            //Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("e:\\100MB1.img");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = sc.getInputStream();
            //No of bytes read in one read() call
            int bytesRead = 0;
            while((bytesRead=is.read(contents))!=-1)
            bos.write(contents, 0, bytesRead);
            bos.flush();
 
            //Recibo el mensaje del servidor
            String mensaje = in.readUTF();
 
            System.out.println(mensaje);
 
            sc.close();
 
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
 
}