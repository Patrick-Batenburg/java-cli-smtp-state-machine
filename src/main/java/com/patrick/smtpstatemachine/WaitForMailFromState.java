package com.patrick.smtpstatemachine;

import com.patrick.SmtpContext;

// Handle "MAIL FROM: <user@domain.nl>" Command & TRANSITION TO NEXT STATE
// Handle "QUIT" Command
// Generate "503 Error on invalid input"
public class WaitForMailFromState implements SmtpStateInf
{
    private SmtpContext context;

    public WaitForMailFromState(SmtpContext context)
    {
        this.context = context;
        context.SendData("250 Hello " + this.context.GetHost());
    }

    @Override
    public void Handle(String data)
    {
        if(data.toUpperCase().startsWith("MAIL FROM: <"))
        {
            if (data.endsWith(">"))
            {
                String email = data.substring(12, data.length() - 1);

                if (SmtpContext.validate(email))
                {
                    this.context.SetMailFrom(email);
                    this.context.SetNewState(new WaitForRcptToState(context));
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