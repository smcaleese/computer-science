from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys

def create_new_client_connections():
    # wait for clients to connect to the server
    while True:
        client, client_address = server_socket.accept()
        print("Connected to {}:{}".format(client_address[0], client_address[1]))
        # start a thread for this new client
        Thread(target=handle_new_client, args=(client,)).start()

# instead of handling new people in the server, this function should accept
def handle_new_client(client):
    client_info = client.recv(BUFFSIZE).decode("utf8")
    name, chatroom_name = client_info.split(":")

    welcome = "Welcome to the {} chatroom {}! Type 'q' to quit".format(chatroom_name, name)
    client.send(bytes(welcome, "utf-8"))
    # create chatroom if it doesn't exist and add user
    if chatroom_name not in chatrooms:
        chatrooms[chatroom_name] = {}
        chatrooms[chatroom_name][client] = name
    else:
        broadcast("{} has joined the chat!".format(name), chatroom_name)
        chatrooms[chatroom_name][client] = name
    # handle messages from a client
    while True:
        client_message = client.recv(BUFFSIZE)
        if client_message != bytes("q", "utf-8"):
            broadcast("{}: {}".format(name, client_message.decode("utf-8")), chatroom_name)
        else:
            print("removing client")
            del chatrooms[chatroom_name][client]
            client.close()
            broadcast("{} has left the chat".format(name), chatroom_name)
            break

def broadcast(client_message, chatroom_name):
    # send a message to everyone in the chatroom
    for socket in chatrooms[chatroom_name]:
        socket.send(bytes(client_message, "utf-8"))

server_socket = 0
BUFFSIZE = 1024
clients = {} # key: socket, value: name
chatrooms = {} # key: chatroom_name, value: a list of users in the chatroom

def main():
    if len(sys.argv[1:]) == 2:
        ip_addr, port = sys.argv[1], int(sys.argv[2])
    else:
        ip_addr, port = "0.0.0.0", 8080

    sock_addr = (ip_addr, port)
    global server_socket
    server_socket = socket(AF_INET, SOCK_STREAM)
    server_socket.bind(sock_addr)
    server_socket.listen(10)  # Listens for at most 10 connections

    print(f"Server started on {ip_addr} {port}")
    print("Waiting for client connections...")
    add_new_clients_thread = Thread(target=create_new_client_connections)
    add_new_clients_thread.start()
    add_new_clients_thread.join()
    server_socket.close()

if __name__ == "__main__":
    main()