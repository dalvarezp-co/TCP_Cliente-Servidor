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
		System.out.println("Cliente conectado");
		in = new DataInputStream(recibe);
		out = new DataOutputStream(saca);

		//Le envio un mensaje
		try {
			out.writeUTF("Hola mundo desde el servidor no: "+id);
			
			//Leo el mensaje que me envia
			String mensaje = in.readUTF();
			
			//total+","+arch +","+descarga
			String[] ms=mensaje.split(",");
			int total= Integer.parseInt(ms[0]);
			String archivo=ms[1];
			while(Servidor.NumeroServidor<total){
				out.writeUTF("Esperando a que se conecten todos los clientes, cliente: "+id);
				barrera.wait();
			}
				
			System.out.println(mensaje);
			//Specify the file
			File file = new File(archivo);
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
				System.out.println("Enviando archivo ... "+(current*100)/fileLength+"% complete!");
			}
			System.out.println("File sent succesfully!: " + id);
			entrada.close();
			salida.close();
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
