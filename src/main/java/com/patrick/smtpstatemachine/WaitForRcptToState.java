package com.patrick.smtpstatemachine;

import com.patrick.SmtpContext;

// Handle "RCPT TO: <user@domain.nl>" Command & TRANSITION TO NEXT STATE
// Handle "QUIT" Command
// Generate "503 Error on invalid input"
public class WaitForRcptToState implements SmtpStateInf
{
    SmtpContext context;

    public WaitForRcptToState(SmtpContext context)
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
                    this.context.SetNewState(new WaitForRcptToOrDataState(context));
                    return;
                }
            }
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