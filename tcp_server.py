import socket
import json
from io import StringIO
from time import ctime

HOST = '39.115.17.14'

PORT = 63635

BUFSIZ = 1024

ADDR = (HOST, PORT)

if __name__ == '__main__':

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_socket.bind(ADDR)

    server_socket.listen(5)

    server_socket.setsockopt( socket.SOL_SOCKET, socket.SO_REUSEADDR, 1 )

    while True:

        print('Server waiting for connection...')

        client_sock, addr = server_socket.accept()
        print("연결수락");
        print('Client connected from: ', addr)

        while True:

            data = client_sock.recv(BUFSIZ)

            if not data or data.decode('utf-8') == 'END':

                break
            print(data)
            data = data.decode('ascii')
            data = data.translate(str.maketrans('', '',  " ?.!/;\0"))
            parsed_json = json.loads(data)
            print("Received from client: %s" % data)
            print("name : " + parsed_json['name'])
            print("company : " + parsed_json['company'])
            print("age : " + parsed_json['age'])
            #.decode('utf- 8')
            #print("Sending the server time to client: %s"  %ctime())

            #try:

             #   client_sock.send(bytes(ctime(), 'utf-8'))

            #except KeyboardInterrupt:

             
             #   print("Exited by user")

        client_sock.close()

    server_socket.close()

