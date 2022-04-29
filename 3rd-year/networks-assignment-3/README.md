# How to Run the Server
To start the server run this command in the terminal
`python3 server.py`

This command will start the server with default arguments so it is the same as typing:

`python3 server.py "0.0.0.0" "8080"`

If this doesn't work try a command in the format:
`python3 server.py "IP_ADDRESS" "PORT_NUMBER"`

For example:
`python3 server.py "127.0.0.1" "8085"`

# How to Start the Client
To start two clients, open two terminals in the directory of the project and type a command of this format in both terminals:
`python3 ui_client.py "YOUR_NAME", "IP_ADDRESS", "PORT", "CHATROOM_NAME"`

For example:
`python3 ui_client.py "Stephen", "127.0.0.1", "8085", "DCU"`

The IP address and port need to be the same as the server.

Typing `python3 ui_client.py` without any arguments will input the default arguments.

So `python3 ui_client.py` is the same as `python3 ui_client.py "Guest" "0.0.0.0" "8080" "Default"`

`ui_client.py` will start the client with a Tkinter GUI. Alternatively, you can run the
client in the terminal with `terminal_client.py` if you want to type messages
in the terminal instead.

# Videos Links

link to folder containing two videos:

https://drive.google.com/drive/folders/1QjZ_PlkaVfn5Z9ner_UaOV_euJgCNxga?usp=sharing

# External Sources

My solution is based on this Python chat room implementation:

https://medium.com/swlh/lets-write-a-chat-app-in-python-f6783a9ac170

