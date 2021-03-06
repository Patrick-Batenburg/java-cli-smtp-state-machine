package com.patrick;

import com.patrick.smtpstatemachine.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmtpContext extends Thread
{
    // statemachine attributes
    private SmtpStateInf statemachine;
    private SmtpStateInf setNewState;

    // socket attributes
    private Socket socket = null;
    private PrintWriter out;

    // mail attributes
    private String hostname;
    private String mailFrom;
    private List<String> rcptTo = new ArrayList<String>();
    private StringBuilder body = new StringBuilder();

    public SmtpContext(Socket socket)
    {
        super("SmtpContextThread");
        this.socket = socket;

        try
        {
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        statemachine = new WelcomeState(this);
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr)
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    /*
        STATE MANIPULATION
        ==================
     */
    public void SetNewState(SmtpStateInf state)
    {
        setNewState = state;
    }

    private void applyNewState()
    {
        if(setNewState!=null)
        {
            statemachine=setNewState;
            setNewState=null;
        }
    }

    /*
        THREADED INPUT PROCESSING
        =========================
     */
    public void run()
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                statemachine.Handle(inputLine);
                System.out.println("C: " + inputLine);

                if (inputLine.toUpperCase().equals("QUIT"))
                {
                    break;
                }

                applyNewState();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
        SOCKETSTREAM MANIPULATION
        =========================
     */
    public void SendData(String data)
    {
        out.println(data);
        System.out.println("S: " + data);
    }

    public void DisconnectSocket()
    {
        try
        {
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
        GETTERS EN SETTERS
        ==================
     */
    public void SetHost(String hostname)
    {
        this.hostname = hostname;
    }

    public String GetHost()
    {
        return hostname;
    }

    public void SetMailFrom(String mailFrom)
    {
        this.mailFrom=mailFrom;
    }

    public String GetMailFrom()
    {
        return mailFrom;
    }

    public void AddRecipient(String recipient)
    {
        rcptTo.add(recipient);
    }

    public void AddTextToBody(String text)
    {
        body.append(text);
    }
}