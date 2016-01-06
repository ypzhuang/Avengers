package models.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "t_sys_user")
public class SysUser extends Model {
    @Id
    public Long id;

    @Column(unique=true)
    @Constraints.Email
    @Constraints.Required
    public String email;

    @Column(unique=true)
    public String phone;

    @JsonIgnore
    @Column(length=64, nullable=false)
    private byte[] password;

    public void setPassword(String password){
        this.password = getSha512(password);
    }

    @JsonIgnore
    @Column(length=64,unique=true)
    public byte[] authToken; //it's better to store token in a sub table for multi device to login

    @JsonIgnore
    public String tempToken; //just for develop, transient at product env

    private static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isDeleted;

    public Date createdDate;

    public Date lastLoginTime;

    @Enumerated(EnumType.STRING)
    public Role role;

    public SysUser() {
        this.createdDate = new Date();
    }

    public SysUser(String email, String password) {
        this.email = email.toLowerCase();
        setPassword(password);
        this.createdDate = new Date();
    }

    public String createToken(){
        String token = UUID.randomUUID().toString();
        this.tempToken = token;
        this.authToken = getSha512(token);
        this.lastLoginTime = new Date();
        save();
        return token;
    }

    public void deleteAuthToken(){
        this.tempToken = null;
        this.authToken = null;
        save();
    }



    public static Finder<Long, SysUser> find = new Finder<>(Long.class, SysUser.class);

    public static SysUser findByAuthToken(String authToken) {
        if (authToken == null || "".equals(authToken)) {
            return null;
        }

        try  {
            return find.where().eq("authToken", getSha512(authToken)).findUnique();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static SysUser findByEmailAndPassword(String email, String password) {
        return SysUser.find.where()
                .eq("email", email.toLowerCase())
                .eq("password", getSha512(password))
                .eq("isDeleted",false)
                .findUnique();
    }

    public static SysUser findByEmail(String email) {
        return SysUser.find.where()
                .eq("email", email.toLowerCase())
                .findUnique();
    }

    public static SysUser findByPhone(String phone) {
        return SysUser.find.where()
                .eq("phone", phone)
                .findUnique();
    }

}
