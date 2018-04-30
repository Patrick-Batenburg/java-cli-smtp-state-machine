package com.patrick.smtpstatemachine;

import com.patrick.SmtpContext;

// handle the receiving of the mailbody
// "\r\n.\r\n" should return to the WaitForMailFromState
public class ReceivingDataState implements SmtpStateInf
{
    private SmtpContext context;

    public ReceivingDataState(SmtpContext context)
    {
        this.context = context;
        this.context.SendData("354 End data with <CR><LF>.<CR><LF>");
    }

    @Override
    public void Handle(String data)
    {
        if (!data.equals(".") || !data.equals("\r\n.\r\n"))
        {
            this.context.AddTextToBody(data + "\n");
        }
        else
        {
            this.context.SetNewState(new WaitForMailFromState(context));
        }
    }
}