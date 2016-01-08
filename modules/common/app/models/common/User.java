package models.common;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by ypzhuang on 16/1/6.
 */

@Entity
@Table(name = "t_user")
public class User extends Model {
    @Id
    public Long id;

    private String username;
    private String password;
    private Integer age;
    private Integer gender;
    private String tel;
    private String email;
    private Date lastLogin;
    private String pic;  //用户头像

    private Integer role; //用户角色
    private Integer clientType;//客户端类型
    private Integer version;//当前用户版本
    private Boolean delete;//是否删除

    private Date createTime;

    private String gid;//用户唯一通讯编号  add by harper 2014-11-02
    private String nickname;//用户昵称 add by harper 2014-11-18

    private String introduction; //个人简介

    private String wechatOpenId; //绑定的微信号unionId

    private String address; //用户填写的地址

    private Float latitude; //纬度

    private Float longitude; //经度

    private String city; //经纬度算出来的当前城市

    private String yizhenId; //yizhenId

    @Column(name="medicinePrivacySetting", columnDefinition="INT default 0")
    private Integer medicinePrivacySetting; //用药隐私设置

    @Column(name="ltrPrivacySetting", columnDefinition="INT default 0")
    private Integer ltrPrivacySetting; //病历隐私设置

    @Column(name="diseasePrivacySetting", columnDefinition="INT default 0")
    private Integer diseasePrivacySetting; //疾病隐私设置


    private Float distance; //KM


    @Transient
    public Float getDistance() {
        return distance;
    }
    public void setDistance(Float distince) {
        this.distance = distince;
    }
    @Column(name="create_time")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getRole() {
        return role;
    }
    public void setRole(Integer role) {
        this.role = role;
    }

    @Column(name="client_type")
    public Integer getClientType() {
        return clientType;
    }
    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name="is_delete")
    public Boolean getDelete() {
        return delete;
    }
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
    @Column(length=64, unique=true, nullable=false)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length=32, nullable=false)
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    @Column(length=16)
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    @Column(length=128)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastLogin")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(length=256)
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(length=128)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    //@Column(name="medicinePrivacySetting")
    public Integer getMedicinePrivacySetting() {
        return medicinePrivacySetting;
    }

    //@Column(name="diseasePrivacySetting")
    public Integer getDiseasePrivacySetting() {
        return diseasePrivacySetting;
    }

    //@Column(name="ltrPrivacySetting")
    public Integer getLtrPrivacySetting() {
        return ltrPrivacySetting;
    }


    public void setMedicinePrivacySetting(Integer medicinePrivacySetting) {
        this.medicinePrivacySetting = medicinePrivacySetting;
    }

    public void setDiseasePrivacySetting(Integer diseasePrivacySetting) {
        this.diseasePrivacySetting = diseasePrivacySetting;
    }

    public void setLtrPrivacySetting(Integer ltrPrivacySetting) {
        this.ltrPrivacySetting = ltrPrivacySetting;
    }
    @Column(name="wechat_openId")
    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Column(name="yizhen_id")
    public String getYizhenId() {
        return yizhenId;
    }

    public void setYizhenId(String yizhenId) {
        this.yizhenId = yizhenId;
    }
    @Override
    public String toString() {
        return "UserEntity [username=" + username + ", password=" + password + ", age=" + age + ", gender=" + gender
                + ", tel=" + tel + ", email=" + email + ", lastLogin=" + lastLogin + ", pic=" + pic + ", role=" + role
                + ", clientType=" + clientType + ", version=" + version + ", delete=" + delete + ", createTime="
                + createTime + ", gid=" + gid + ", nickname=" + nickname + ", introduction=" + introduction
                + ", wechatOpenId=" + wechatOpenId + ", address=" + address + ", latitude=" + latitude + ", longitude="
                + longitude + ", city=" + city + ", yizhenId=" + yizhenId + ", medicinePrivacySetting="
                + medicinePrivacySetting + ", ltrPrivacySetting=" + ltrPrivacySetting + ", diseasePrivacySetting="
                + diseasePrivacySetting + ", distance=" + distance + "]";
    }


    public static Finder<Long, User> find = new Finder<>(Long.class, User.class);


    public static List<User> search(String filter,int page,int pageSize) {
        return getPage(filter, page, pageSize).getList();
    }

    public static int count(String filter, int page,int pageSize) {
        return getPage(filter, page, pageSize).getTotalRowCount();
    }


    private static Page<User> getPage(String filter,int page,int pageSize){
        Logger.debug("search User with param [{}]", filter, page, pageSize);

        ExpressionList<User> expression = User.find.where();

        if (filter != null && !"".equals(filter)) {
            expression.or(
                    Expr.ilike("username", "%" + filter + "%"),
                    Expr.like("tel", "%" + filter + "%")
            );
        }

        expression.eq("delete",false);

        return expression
                .orderBy("id desc")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }


    public static String findUserNameByUserId(Long userId){
        User user = User.find.byId(userId);
        if (user != null) {
            return user.username != null ? user.username : user.getNickname();
        } else {
            return "";
        }
    }

}
