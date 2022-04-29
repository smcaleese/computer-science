from socket import AF_INET, socket, SOCK_STREAM
from threading import Thread
import sys
import tkinter

def run_client(name, chatroom_name):
    # tell the server your name and the name of the chatroom you want to join
    client_info = name + ":" + chatroom_name
    create_user(client_info)
    # start listening for messages from others users
    receive_thread = Thread(target=receive_messages)
    receive_thread.start()

def receive_messages():
    while True:
        try:
            message_from_server = client_socket.recv(BUFFSIZE).decode("utf8")
            previous_messages_list.insert(tkinter.END, message_from_server)
        except OSError:
            break

def create_user(client_info):
    client_socket.send(bytes(client_info, "utf-8"))

def send_message(event=None):
    """Handles sending of messages."""
    msg = message_var.get()
    message_var.set("")  # Clears input field.
    client_socket.send(bytes(msg, "utf8"))
    if msg == "q":
        client_socket.close()
        box.quit()

def on_closing(event=None):
    """This function is to be called when the window is closed."""
    message_var.set("q")
    send_message()

# variables for socket
client_socket = 0
client_thread = 0
BUFFSIZE = 1024

# create tkinter box
box = tkinter.Tk()
box.title("Chat")
messages_frame = tkinter.Frame(box)
message_var = tkinter.StringVar() # variable to store user input
message_var.set("")
scrollbar = tkinter.Scrollbar(messages_frame)
previous_messages_list = tkinter.Listbox(messages_frame, height=15, width=60, yscrollcommand=scrollbar.set)
scrollbar.pack(side=tkinter.RIGHT, fill=tkinter.Y)
previous_messages_list.pack(side=tkinter.LEFT, fill=tkinter.BOTH)
previous_messages_list.pack()
messages_frame.pack()
entry_field = tkinter.Entry(box, textvariable=message_var)
entry_field.bind("<Return>", send_message)
entry_field.pack()
send_button = tkinter.Button(box, text="Send", command=send_message)
send_button.pack()
# call this function when the window is closed
box.protocol("WM_DELETE_WINDOW", on_closing)

def main():
    if len(sys.argv[1:]) == 4:
        name, ip_addr, port, chatroom_name = sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4]
    else:
        name, ip_addr, port, chatroom_name = "Guest", "0.0.0.0", 8080, "Default"

    # create client socket
    sock_addr = (ip_addr, port)
    global client_socket
    client_socket = socket(AF_INET, SOCK_STREAM)
    # connect to server
    client_socket.connect(sock_addr)
    # start listening for messages from other users
    global client_thread
    client_thread = Thread(target=run_client, args=(name, chatroom_name))
    # start socket
    client_thread.setDaemon(True)
    client_thread.start()
    # start tkinter GUI
    tkinter.mainloop()
    client_thread.join()
    client_socket.close()

if __name__ == "__main__":
    main()