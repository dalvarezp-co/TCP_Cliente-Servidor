# TCP_Cliente-Servidor
Para ejecutar este modelo de Cliente-Servidor TCP lo que se debe realizar es:
1. Instalar java en el Servidor FTP
2. Clonar repositorio con github en el Servidor FTP
3. En la carpeta TCP_Cliente-Servidor crear 2 archivos, uno de 100MB y otro de 250MB de la siguiente manera:
dd if=/dev/zero of=./100MB.txt bs=1024 count=102400
dd if=/dev/zero of=./250MB.txt bs=1024 count=256000
4. Después de esto, ejecute el Servidor con el comando
java Servidor
5. Cuando el Servidor se esté ejecutando digite cuántas conexiones concurrentes desea hacer (recuerde este número ya que será usado más adelante)
6. Ya con el servidor ejecutandose, busque la constante HOST en la clase Cliente y cámbiela dependiendo de la ip de su servidor
7. Finalmente ejecute la clase ThreadsClientes, en esta clase le harán 3 preguntas:
Cuántos clientes desea poner? (Poner los mismos del servidor)
Que archivo desea escoger: 1. 100MB 2. 250MB
Cuántos clientes desea que descarguen el archivo? (Cuántos clientes quiere enviarle el archivo en simultáneo)
8. Durante el proceso se le preguntará si los clientes están listos para recibir el archivo, dé un enter por cada cliente que puso que iba a descargar en simultáneo.
9. Después los archivos descargados saldrán en la carpeta ArchivosRecibidos y los log de esas transacciones en la carpeta logs

Muchas gracias :)
