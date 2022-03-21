//Hola
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Cliente extends Thread{
	
	public int id;
	public String arch;
	public int total;
	private static BufferedWriter escritorParaLOG;
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
        final String HOST = "192.168.157.128";
        //Puerto del servidor
        final int PUERTO = 5000;
        DataInputStream in;
        DataOutputStream out;
 
        try {
        	BufferedReader lector = new BufferedReader (new InputStreamReader (System.in));
        	String fechaHoy = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
    		File archivoLog = new File("logs/" + fechaHoy + "-log.txt");
    		escritorParaLOG = new BufferedWriter(new FileWriter(archivoLog));
        	
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
            	Thread.sleep(100);
            	System.out.println("Como cliente "+id+" voy a confirmar que estoy listo para recibir el archivo: Presione Enter");
        		String listo= lector.readLine();
        		out.writeUTF(listo);
        		long inicioEnvio = System.currentTimeMillis();
            	byte[] hashRecibido=readBytes(in);
            	System.out.println("Hash recibido para el cliente: "+id);
            	byte[] contents = new byte[100000];
            	String pathDescarga="";
            	pathDescarga="ArchivosRecibidos/Cliente"+id+"-Prueba-"+total+".txt";
            	FileOutputStream fos = new FileOutputStream(pathDescarga);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = sc.getInputStream();
                //No of bytes read in one read() call
                int bytesRead = 0;
                while((bytesRead=is.read(contents))!=-1)
                bos.write(contents, 0, bytesRead);
                bos.flush();
                long finalDescarga = System.currentTimeMillis();
     
                out.writeUTF("Se descargo el archivo");

                sc.close();
                System.out.println("Archivo guardado con exito para el cliente: "+id);
                String hash=new String(hashRecibido, StandardCharsets.UTF_8);
                File fileDescargado = new File(pathDescarga);
                String hashArchivo=hash(fileDescargado);
                String siModificado="";
                if(hash.equals(hashArchivo)){
                	System.out.println("Archivo no modificado para el cliente: "+id);
                	siModificado="sin modificar";
                } else {
                	System.out.println("Archivo no modificado para el cliente: "+id);
                	siModificado="modificado";
                }
                
                escritorParaLOG.write("El cliente "+ id +" recibio el archivo: " 
                + fileDescargado.getName() + " de tamano: " + fileDescargado.length() + " bytes, " +
                		"con una entrega exitosa, "+ siModificado+" y con un tiempo de transferencia de: "
                		+ (finalDescarga-inicioEnvio) + " milisegundos");
                escritorParaLOG.newLine();
                escritorParaLOG.flush();
                
                
            }
            else {
            	System.out.println("Archivo no guardado para el cliente: "+id);
            	escritorParaLOG.write("El cliente "+ id +" no recibio el archivo ya que no lo solicito");
                        escritorParaLOG.newLine();
                        escritorParaLOG.flush();
            }
            
 
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public String hash(File file){
		byte[] bytes=null;
		String hashFinal="";
		try {
			bytes = Files.readAllBytes(file.toPath());
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] result = md.digest(bytes);
			hashFinal = new String(result, StandardCharsets.UTF_8);
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashFinal;
		

	}
    
    public byte[] readBytes(DataInputStream in) throws IOException {
        int len = in.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            in.readFully(data);
        }
        return data;
    }
 
}