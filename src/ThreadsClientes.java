import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadsClientes {
	public static void main(String args[]) throws IOException {
		BufferedReader lector = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("Hola, cuántos clientes desea poner: ");
		int cantClientes= Integer.parseInt(lector.readLine());
		System.out.println("Que archivo desea escoger: 1. 100MB 2. 250MB");
		int archivo= Integer.parseInt(lector.readLine());
		String arch="";
		if(archivo==1){
			arch="../100MB.txt";
		}
		else arch="../250MB.txt";
		System.out.println("Hola, cuántos clientes desea que descarguen el archivo: ");
		int cantClientesDesc= Integer.parseInt(lector.readLine());
		Cliente[] clientes=new Cliente[cantClientes];

		for(int i=0; i<cantClientes;i++){
			if(i<cantClientesDesc){
				clientes[i]=new Cliente(i,arch, "Si",cantClientes);
			}
			else {
				clientes[i]=new Cliente(i,arch, "No", cantClientes);
			}
				
			clientes[i].start();
		}
	}
}
