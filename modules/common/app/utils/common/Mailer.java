package utils.common;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

/**
 * Created by ypzhuang on 15/10/14.
 */
public abstract class Mailer {
    private MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();

    private String subject;
    private String recipients[];
    private String from;

    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setRecipient(String... recipients){
        this.recipients = recipients;
    }
    public void setFrom(String from){
        this.from = from;
    }


    public void sendHtml(String bodyHtml) {
        mail.setSubject(this.subject);
        mail.setRecipient(this.recipients);
        mail.setFrom(this.from);
        mail.sendHtml(bodyHtml);
    }

    public void send(String bodyText) {
        mail.setSubject(this.subject);
        mail.setRecipient(this.recipients);
        mail.setFrom(this.from);
        mail.send(bodyText);
    }



}
