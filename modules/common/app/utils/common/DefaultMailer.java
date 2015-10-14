package utils.common;

import play.Play;

/**
 * Created by ypzhuang on 15/10/14.
 */
public class DefaultMailer extends Mailer {

    public DefaultMailer(){
        super();
        String from = Play.application().configuration().getString("smtp.from");
        setFrom(from);
    }


}
