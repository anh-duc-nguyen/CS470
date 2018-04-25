import socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(('localhost', 8888))
message = b'GET http://www.google.com/ HTTP/1.1\r\nIf-Modified-Since:wed,25 Apr 2018 13:00 GMT\r\n'
s.sendall(message)
data = s.recv(1000000)
print(data[:100])