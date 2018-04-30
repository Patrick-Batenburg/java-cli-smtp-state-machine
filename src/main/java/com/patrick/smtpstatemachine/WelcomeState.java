package com.patrick.smtpstatemachine;

import com.patrick.SmtpContext;

public class WelcomeState implements SmtpStateInf
{
    private SmtpContext context;

    public WelcomeState(SmtpContext smtpContext)
    {
        this.context = smtpContext;
        this.context.SendData("220 smtp.example.com Welcome at this amazing smtp server!");
    }

    @Override
    public void Handle(String data)
    {
        if(data.toUpperCase().startsWith("HELO"))
        {
            this.context.SetHost(data.substring(5));
            this.context.SendData("250 Hello " + context.GetHost() + ", I am glad to meet you");
            this.context.SetNewState(new WaitForMailFromState(context));
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