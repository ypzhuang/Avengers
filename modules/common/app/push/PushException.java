package push;

/**
 * Created by ypzhuang on 15/12/7.
 */
public class PushException extends Exception {

    public PushException(){
        this("");
    }

    public PushException(String message){
        super("push error:"+message);
    }

}
