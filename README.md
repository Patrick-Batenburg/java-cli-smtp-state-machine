# SMTP state machine

# Enabling Telnet in Windows
1.  Open **Control Panel**.
2.  Goto **Programs and Features**.
3.  From the left side of the next page, click/tap the **Turn Windows features on or off** link.
4.  From the Windows Features window, select the box next to **Telnet Client**.
5.  Click/tap **OK** to enable Telnet.

# Connecting to the server
1.  Run the server and let it listen on the port it is assigned (default 25).
2.  Open a command prompt and type _telnet host port_ (telnet 127.0.0.1 25)