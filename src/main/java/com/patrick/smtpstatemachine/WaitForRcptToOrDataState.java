package com.patrick.smtpstatemachine;

import com.patrick.SmtpContext;

// Handle "MAIL FROM: <user@domain.nl>" Command & TRANSITION TO NEXT STATE
// Handle "DATA" Command & TRANSITION TO NEXT STATE
// Handle "QUIT" Command
// Generate "503 Error on invalid input"
public class WaitForRcptToOrDataState implements SmtpStateInf
{
    private SmtpContext context;

    public WaitForRcptToOrDataState(SmtpContext context)
    {
        this.context = context;
        this.context.SendData("250 OK");
    }

    @Override
    public void Handle(String data)
    {
        if(data.toUpperCase().startsWith("RCPT TO: <"))
        {
            if (data.endsWith(">"))
            {
                String email = data.substring(10, data.length() - 1);

                if (SmtpContext.validate(email))
                {
                    this.context.AddRecipient(email);
                    return;
                }
            }
        }

        if(data.toUpperCase().startsWith("DATA"))
        {
            this.context.SetNewState(new ReceivingDataState(context));
            return;
        }

        if(data.toUpperCase().startsWith("QUIT"))
        {
            this.context.SendData("221 Bye");
            this.context.DisconnectSocket();
            return;
        }

        this.context.SendData("503 Error...");
    }
}