from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys

def run_client(name, chatroom_name):
    # tell the server your name and the name of the chatroom you want to join
    client_info = name + ":" + chatroom_name
    send_message(client_info)
    # start listening for messages from others users
    receive_thread = Thread(target=receive_messages)
    receive_thread.start()
    # send messages to the server
    new_message = input()
    while True:
        if new_message != "q":
            send_message(new_message)
            new_message = input()
        else:
            send_message(new_message) # tell the server that we're leaving it
            client_socket.close()
            break

def receive_messages():
    while True:
        try:
            message_from_server = client_socket.recv(BUFFSIZE).decode("utf8")
            print(message_from_server)
        except OSError:
            break

def send_message(new_message):
    client_socket.send(bytes(new_message, "utf-8"))

client_socket = 0
client_thread = 0
BUFFSIZE = 1024

def main():
    if len(sys.argv[1:]) == 4:
        name, ip_addr, port, chatroom_name = sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4]
    else:
        name, ip_addr, port, chatroom_name = "Guest", "0.0.0.0", 8080, "Default"

    sock_addr = (ip_addr, port)
    global client_socket
    client_socket = socket(AF_INET, SOCK_STREAM)
    # connect to server
    client_socket.connect(sock_addr)
    # start listening for messages from other users
    global client_thread
    client_thread = Thread(target=run_client, args=(name, chatroom_name))
    client_thread.start()
    client_thread.join()
    client_socket.close()

if __name__ == "__main__":
    main()