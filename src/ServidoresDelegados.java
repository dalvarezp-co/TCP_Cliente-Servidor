import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CyclicBarrier;


public class ServidoresDelegados extends Thread{
	/**
	 * Lo que recibe del Cliente
	 */
	private InputStream recibe;
	private BufferedReader entrada;
	/**
	 * Lo que manda al Cliente
	 */
	private OutputStream saca;
	private PrintWriter salida;
	/**
	 * Conexion con el cliente
	 */
	private Socket sc;
	public String mensajePedido;
	public int id;
	DataInputStream in;
	DataOutputStream out;
	/**
	 * Barrera que va a permitir esperar a que todos esten conectados
	 */
	private CyclicBarrier barrera;

	/**
	 * Metodo constructor que crea un servidor delegado dado una conexion con un cliente
	 */
	public ServidoresDelegados(Socket clienteAtendiendo, int id){
		this.id=id;

		sc=clienteAtendiendo;
		try{
			saca=sc.getOutputStream();
			recibe=sc.getInputStream();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		InputStreamReader in=new InputStreamReader(recibe);
		entrada= new BufferedReader(in);
		salida = new PrintWriter(saca, true);
	}
	public void run(){
		System.out.println("Cliente conectado: "+ id );
		in = new DataInputStream(recibe);
		out = new DataOutputStream(saca);

		//Le envio un mensaje
		try {
			out.writeUTF("Hola mundo desde el servidor no: "+id);

			//Leo el mensaje que me envia
			String mensaje = in.readUTF();

			//total+","+arch +","+descarga
			String[] ms=mensaje.split(",");

			String archivo=ms[1];
			String siDescarga=ms[2];

			//Specify the file
			if(siDescarga.equals("Si")){
				mensaje = in.readUTF();
				File file = new File(archivo);
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] hashArch=hash(file);
				sendBytes(hashArch, 0, hashArch.length, out);
				//Get socket's output stream
				OutputStream os = sc.getOutputStream();
				//Read File Contents into contents array
				byte[] contents;
				long fileLength = file.length();
				long current = 0;
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
					//System.out.println("Enviando archivo ... "+(current*100)/fileLength+"% complete!");
				}
				System.out.println("Archivo enviado con exito para el cliente: " + id);
				System.out.println("Cliente desconectado: "+ id );
			}
			else {
				System.out.println("No se descargo nada en el cliente:  " + id);
			}
			entrada.close();
			salida.close();
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public byte[] hash(File file){
		byte[] bytes=null;
		byte[] result = null;
		try {
			bytes = Files.readAllBytes(file.toPath());
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			result = md.digest(bytes);			
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;


	}

	public void sendBytes(byte[] myByteArray, int start, int len, DataOutputStream dos) throws IOException {
		if (len < 0)
			throw new IllegalArgumentException("Negative length not allowed");
		if (start < 0 || start >= myByteArray.length)
			throw new IndexOutOfBoundsException("Out of bounds: " + start);

		dos.writeInt(len);
		if (len > 0) {
			dos.write(myByteArray, start, len);
		}
	}
}
