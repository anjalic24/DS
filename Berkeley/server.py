import socket, time
from datetime import datetime

HOST, PORT = '', 9999

s = socket.socket()
s.bind((HOST, PORT))
s.listen(10)

num = int(input("Enter number of clients: "))
clients = [s.accept()[0] for _ in range(num)]

# THIS INFINITE LOOP IS A DAEMON (SIMPLIFIED)
while True:
    times = []

    for c in clients:
        times.append(float(c.recv(1024).decode()))

    server_time = time.time()
    avg = (sum(times) + server_time) / (len(times) + 1)

    for i, t in enumerate(times):
        print(f"Client {i+1} Time:", datetime.fromtimestamp(t).strftime('%H:%M:%S'))

    print("Server Time:", datetime.fromtimestamp(server_time).strftime('%H:%M:%S'))
    print("Average Time:", datetime.fromtimestamp(avg).strftime('%H:%M:%S'))
    print("\n")

    for i, c in enumerate(clients):
        c.send(str(avg - times[i]).encode())